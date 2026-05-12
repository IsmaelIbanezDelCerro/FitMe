package com.example.fitme.screens

import android.location.Geocoder
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.BorderStroke
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
import com.example.fitme.LocalAppStrings
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun GimnasiosScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var ciudad by remember { mutableStateOf("") }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var markerTitle by remember { mutableStateOf("") }
    var notFound by remember { mutableStateOf(false) }

    // Default: Madrid, Spain
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.4168, -3.7038), 6f)
    }

    fun buscar() {
        val ciudadFinal = ciudad.trim()
        if (ciudadFinal.isEmpty()) return
        keyboard?.hide()
        notFound = false
        scope.launch {
            @Suppress("DEPRECATION")
            val addresses = withContext(Dispatchers.IO) {
                runCatching {
                    Geocoder(context, Locale.getDefault()).getFromLocationName(ciudadFinal, 1)
                }.getOrNull()
            }
            val address = addresses?.firstOrNull()
            if (address != null) {
                val latLng = LatLng(address.latitude, address.longitude)
                markerPosition = latLng
                markerTitle = "Gimnasios en $ciudadFinal"
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
            } else {
                notFound = true
            }
        }
    }

    fun abrirEnMaps() {
        val ciudadFinal = ciudad.trim()
        if (ciudadFinal.isEmpty()) return
        CustomTabsIntent.Builder().setShowTitle(true).build().launchUrl(
            context,
            Uri.parse("https://www.google.com/maps/search/gimnasio+${Uri.encode(ciudadFinal)}")
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            markerPosition?.let { pos ->
                Marker(
                    state = MarkerState(position = pos),
                    title = markerTitle,
                    snippet = "Toca para ver en Google Maps"
                )
            }
        }

        // Search card overlaid at top
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
                    onValueChange = { ciudad = it; notFound = false },
                    label = { Text("Ciudad", color = Color.White.copy(alpha = 0.7f)) },
                    placeholder = { Text("Ej: Getafe, Madrid...", color = Color.White.copy(alpha = 0.3f)) },
                    singleLine = true,
                    isError = notFound,
                    supportingText = if (notFound) {
                        { Text("Ciudad no encontrada", color = Color(0xFFFF5252)) }
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { buscar() },
                        enabled = ciudad.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00C853),
                            disabledContainerColor = Color(0xFF00C853).copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Centrar mapa",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Button(
                        onClick = { abrirEnMaps() },
                        enabled = ciudad.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1565C0),
                            disabledContainerColor = Color(0xFF1565C0).copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Ver en Maps",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Back button at bottom
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(strings.backBtn, fontWeight = FontWeight.Bold)
        }
    }
}
