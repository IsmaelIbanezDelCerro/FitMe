package com.example.fitme.place

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("name")    val name: String,
    @SerializedName("lat")     val lat: Double,
    @SerializedName("lng")     val lng: Double,
    @SerializedName("address") val address: String,
    @SerializedName("rating")  val rating: Double
) {
    fun toPlace() = Place(
        name = name,
        latLng = LatLng(lat, lng),
        address = address,
        rating = rating.toFloat()
    )
}
