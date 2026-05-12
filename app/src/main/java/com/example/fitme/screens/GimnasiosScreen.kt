package com.example.fitme.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.fitme.MarkerInfoWindowAdapter
import com.example.fitme.R
import com.example.fitme.place.Place
import com.example.fitme.place.PlaceRenderer
import com.example.fitme.place.PlacesReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GimnasiosScreen(onVolver: () -> Unit) {
    val context = LocalContext.current
    val places = remember { PlacesReader(context).read() }
    val circleHolder = remember { arrayOfNulls<Circle>(1) }
    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        MapEffect(Unit) { map ->
            if (places.isNotEmpty()) {
                val bounds = LatLngBounds.builder()
                places.forEach { bounds.include(it.latLng) }
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
            }

            val clusterManager = ClusterManager<Place>(context, map)
            clusterManager.renderer = PlaceRenderer(context, map, clusterManager)
            clusterManager.markerCollection.setInfoWindowAdapter(
                MarkerInfoWindowAdapter(context)
            )
            clusterManager.addItems(places)
            clusterManager.cluster()

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
}
