package com.gretea5.finder.data.model

import com.gretea5.finder.data.model.kakao.Document
import com.gretea5.finder.data.model.kakao.Meta

data class AddressResponse(
    val meta: Meta,
    val documents: List<Document>
)