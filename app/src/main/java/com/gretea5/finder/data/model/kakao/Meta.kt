package com.gretea5.finder.data.model.kakao

data class Meta(
    val same_name: SameName,
    val pageable_count: Int,
    val total_count: Int,
    val is_end: Boolean
)