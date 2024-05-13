package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("hpID") val hpID: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)
