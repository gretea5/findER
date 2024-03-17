package com.gretea5.finder.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gretea5.finder.data.model.SignupModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIS {

    @POST("/api/signup")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun signUp(@Body signupModel: SignupModel) : Call<String>

//    @POST("/login")
//    @Headers("accept: application/json",
//        "content-type: application/json")
//    fun signIn(@Body signinModel: SigninModel) : Call<String>

    companion object {
        private const val BASE_URL = "http://13.125.1.150:8080"

        fun create(): APIS {
            val gson : Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }

}