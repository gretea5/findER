package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    //전화번호
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    //주민번호
    @SerializedName("rrn")
    var rrn: String? = null,
)
