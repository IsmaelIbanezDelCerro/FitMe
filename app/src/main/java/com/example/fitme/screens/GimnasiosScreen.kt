package com.example.fitme.screens

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.fitme.LocalAppStrings
import com.example.fitme.MarkerInfoWindowAdapter
import com.example.fitme.R
import com.example.fitme.place.Place
import com.example.fitme.place.PlaceRenderer
import com.example.fitme.place.PlacesReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GimnasiosScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var ciudad by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var buscando by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()
    val circleHolder = remember { arrayOfNulls<Circle>(1) }
    val clusterManagerRef = remember { arrayOfNulls<ClusterManager<Place>>(1) }
    val initialPlaces = remember { PlacesReader(context).read() }

    fun buscar() {
        val ciudadFinal = ciudad.trim()
        if (ciudadFinal.isEmpty()) return
        keyboard?.hide()
        errorMsg = ""
        buscando = true
        scope.launch {
            // 1. Geocodificar ciudad
            @Suppress("DEPRECATION")
            val addresses = withContext(Dispatchers.IO) {
                runCatching {
                    Geocoder(context, Locale.getDefault()).getFromLocationName(ciudadFinal, 1)
                }.getOrNull()
            }
            val address = addresses?.firstOrNull()
            if (address == null) {
                errorMsg = strings.gymNotFound
                buscando = false
                return@launch
            }
            val lat = address.latitude
            val lon = address.longitude

            // 2. Mover cámara a la ciudad inmediatamente
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 12f))

            // 3. Buscar gimnasios en Overpass API
            val gyms = try {
                withContext(Dispatchers.IO) {
                    fetchGymsFromOverpass(lat, lon)
                }
            } catch (_: Exception) {
                errorMsg = "${strings.gymNoResults} $ciudadFinal"
                buscando = false
                return@launch
            }

            // 4. Actualizar ClusterManager con los gimnasios encontrados
            clusterManagerRef[0]?.let { cm ->
                cm.clearItems()
                if (gyms.isNotEmpty()) {
                    cm.addItems(gyms)
                    cm.cluster()
                    if (gyms.size == 1) {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(gyms[0].latLng, 15f)
                        )
                    } else {
                        val bounds = LatLngBounds.builder()
                        gyms.forEach { bounds.include(it.latLng) }
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngBounds(bounds.build(), 100)
                        )
                    }
                } else {
                    cm.cluster()
                    errorMsg = "${strings.gymNoResults} $ciudadFinal"
                }
            }
            buscando = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            MapEffect(Unit) { map ->
                // Carga inicial con los gimnasios del JSON (Madrid)
                if (initialPlaces.isNotEmpty()) {
                    map.setOnMapLoadedCallback {
                        val bounds = LatLngBounds.builder()
                        initialPlaces.forEach { bounds.include(it.latLng) }
                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
                    }
                }

                val clusterManager = ClusterManager<Place>(context, map)
                clusterManager.renderer = PlaceRenderer(context, map, clusterManager)
                clusterManager.markerCollection.setInfoWindowAdapter(
                    MarkerInfoWindowAdapter(context)
                )
                clusterManager.addItems(initialPlaces)
                clusterManager.cluster()
                clusterManagerRef[0] = clusterManager

                clusterManager.setOnClusterItemClickListener { item ->
                    circleHolder[0]?.remove()
                    circleHolder[0] = map.addCircle(
                        CircleOptions()
                            .center(item.latLng)
                            .radius(1000.0)
                            .fillColor(
                                ContextCompat.getColor(context, R.color.colorPrimaryTranslucent)
                            )
                            .strokeColor(
                                ContextCompat.getColor(context, R.color.colorPrimary)
                            )
                    )
                    false
                }

                map.setOnCameraMoveStartedListener {
                    clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
                    clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
                }

                map.setOnCameraIdleListener {
                    clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
                    clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }
                    clusterManager.onCameraIdle()
                }
            }
        }

        // Buscador superpuesto arriba
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A2E).copy(alpha = 0.93f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🗺️", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        strings.cardGimnasioTitle,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = ciudad,
                    onValueChange = { ciudad = it; errorMsg = "" },
                    label = { Text(strings.gymSearchLabel, color = Color.White.copy(alpha = 0.7f)) },
                    placeholder = {
                        Text(strings.gymSearchPlaceholder, color = Color.White.copy(alpha = 0.3f))
                    },
                    singleLine = true,
                    isError = errorMsg.isNotEmpty(),
                    supportingText = if (errorMsg.isNotEmpty()) {
                        { Text(errorMsg, color = Color(0xFFFF5252)) }
                    } else null,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = { buscar() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00C853),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color(0xFF00C853),
                        cursorColor = Color(0xFF00C853)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { buscar() },
                    enabled = ciudad.isNotBlank() && !buscando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C853),
                        disabledContainerColor = Color(0xFF00C853).copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (buscando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            strings.gymSearchBtn,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

private suspend fun fetchGymsFromOverpass(lat: Double, lon: Double): List<Place> =
    withContext(Dispatchers.IO) {
        val radius = 5000
        val query = """
            [out:json][timeout:25];
            (
              node["leisure"="fitness_centre"](around:$radius,$lat,$lon);
              way["leisure"="fitness_centre"](around:$radius,$lat,$lon);
              node["amenity"="gym"](around:$radius,$lat,$lon);
              way["amenity"="gym"](around:$radius,$lat,$lon);
            );
            out center;
        """.trimIndent()

        val encoded = URLEncoder.encode(query, "UTF-8")
        val response = URL("https://overpass-api.de/api/interpreter?data=$encoded").readText()
        val elements = JSONObject(response).getJSONArray("elements")

        (0 until elements.length()).mapNotNull { i ->
            val el = elements.getJSONObject(i)
            val type = el.optString("type")
            if (type != "node" && type != "way") return@mapNotNull null

            val placeLat = if (type == "node") el.optDouble("lat", Double.NaN)
                           else el.optJSONObject("center")?.optDouble("lat", Double.NaN) ?: Double.NaN
            val placeLon = if (type == "node") el.optDouble("lon", Double.NaN)
                           else el.optJSONObject("center")?.optDouble("lon", Double.NaN) ?: Double.NaN
            if (placeLat.isNaN() || placeLon.isNaN()) return@mapNotNull null

            val tags = el.optJSONObject("tags")
            val name = tags?.optString("name")?.takeIf { it.isNotEmpty() } ?: "Gimnasio"
            val street = tags?.optString("addr:street", "") ?: ""
            val number = tags?.optString("addr:housenumber", "") ?: ""
            val city = tags?.optString("addr:city", "") ?: ""
            val address = listOfNotNull(
                if (street.isNotEmpty()) "${street}${if (number.isNotEmpty()) " $number" else ""}" else null,
                city.takeIf { it.isNotEmpty() }
            ).joinToString(", ")

            Place(name, LatLng(placeLat, placeLon), address)
        }
    }
