package com.gretea5.finder.data

import com.gretea5.finder.data.model.AddressResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoApiService {
    @GET("v2/local/search/keyword.json")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun searchAddress(
        @Header("Authorization") apiKey: String,
        @Query("query") address: String
    ): Call<AddressResponse>
}