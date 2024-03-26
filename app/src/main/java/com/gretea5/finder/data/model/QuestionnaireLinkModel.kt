package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName

data class QuestionnaireLinkModel(
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("linkedSerialNumber") var linkedSerialNumber: String = "",
)