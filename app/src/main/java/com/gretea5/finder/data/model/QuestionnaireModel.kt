package com.gretea5.finder.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class QuestionnaireModel(
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("age") var age: Int = 0,
    @SerializedName("gender") var gender: String = "",
    @SerializedName("bloodType") var bloodType: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("allergy") var allergy: String = "",
    @SerializedName("disease") var disease: String = "",
    @SerializedName("medicine") var medicine: String = "",
    @SerializedName("surgery") var surgery: String = "",
    @SerializedName("drink") var drink: String = "",
    @SerializedName("smoke") var smoke: String = "",
    @SerializedName("etc") var etc: String = ""
) : Serializable