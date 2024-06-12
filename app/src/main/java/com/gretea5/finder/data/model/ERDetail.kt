package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class ERDetail(
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("mapAddress") val mapAddress: String,
    @SerializedName("tel") val tel: String,
    @SerializedName("ambulance") val ambulance: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("subject") val subject: String,
    @SerializedName("bedCount") val bedCount: Int,
    @SerializedName("bedTime") val bedTime: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("duration") val duration: String,
    @SerializedName("ertel") val ertel: String,
    @SerializedName("ct") val ct: String,
    @SerializedName("mri") val mri: String
)
