package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class QuestionnaireUnlinkModel (
    @SerializedName("phoneNumber1") var phoneNumber1: String? = null,
    @SerializedName("phoneNumber2") var phoneNumber2: String? = null,
)
