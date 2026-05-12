package com.example.fitme.place

import com.google.android.gms.maps.model.LatLng

data class PlaceResponse(
    val name: String,
    val lat: Double,
    val lng: Double,
    val address: String,
    val rating: Float
) {
    fun toPlace() = Place(
        name = name,
        latLng = LatLng(lat, lng),
        address = address,
        rating = rating
    )
}
