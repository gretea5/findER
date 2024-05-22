package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class ERPreview(
    @SerializedName("hpID") val hpID: String,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("tel") val tel: String,
    @SerializedName("bedCount") val bedCount: Int,
    @SerializedName("bedTime") val bedTime: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("eta") val eta: String,
    @SerializedName("ertel") val ertel: String
)