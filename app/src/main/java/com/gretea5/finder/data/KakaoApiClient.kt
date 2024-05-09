package com.gretea5.finder.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object KakaoApiClient {
    private val gson : Gson = GsonBuilder().setLenient().create()
    private const val BASE_URL = "https://dapi.kakao.com/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getClient(): Retrofit = retrofit
}