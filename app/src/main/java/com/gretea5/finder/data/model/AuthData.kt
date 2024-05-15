package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("phoneNumber") var phoneNumber: String? = null,
    @SerializedName("rrn")  var rrn: String? = null,
)