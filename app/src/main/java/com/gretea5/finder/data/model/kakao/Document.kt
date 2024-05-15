package com.gretea5.finder.data.model.kakao

data class Document(
    val place_name: String,
    val distance: String,
    val place_url: String,
    val category_name: String,
    val address_name: String,
    val road_address_name: String,
    val id: String,
    val phone: String,
    val category_group_code: String,
    val category_group_name: String,
    val x: String,
    val y: String
)