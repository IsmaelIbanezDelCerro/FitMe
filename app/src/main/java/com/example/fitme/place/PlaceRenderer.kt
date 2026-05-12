package com.example.fitme.place

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.fitme.BitmapHelper
import com.example.fitme.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class PlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Place>
) : DefaultClusterRenderer<Place>(context, map, clusterManager) {

    private val fitnessIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context, R.color.colorPrimary)
        BitmapHelper.vectorToBitmap(context, R.drawable.ic_fitness_center, color)
    }

    override fun onBeforeClusterItemRendered(item: Place, markerOptions: MarkerOptions) {
        markerOptions
            .title(item.name)
            .position(item.latLng)
            .icon(fitnessIcon)
    }

    override fun onClusterItemRendered(clusterItem: Place, marker: Marker) {
        marker.tag = clusterItem
    }
}
