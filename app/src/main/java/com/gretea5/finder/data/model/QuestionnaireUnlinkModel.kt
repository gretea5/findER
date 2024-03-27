package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class QuestionnaireUnlinkModel (
    //전화번호
    @SerializedName("phoneNumber1")
    var phoneNumber1: String? = null,
    //주민번호
    @SerializedName("phoneNumber2")
    var phoneNumber2: String? = null,
)
