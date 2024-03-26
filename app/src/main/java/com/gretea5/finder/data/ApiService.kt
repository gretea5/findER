package com.gretea5.finder.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gretea5.finder.data.model.LoginModel
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.data.model.SignupModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/api/signup")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun signUp(@Body signupModel: SignupModel) : Call<String>

    @POST("/api/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun login(@Body loginModel: LoginModel) : Call<String>

    @POST("/api/questionnaire")
    fun writeQuestionnaire(@Body questionnaireModel: QuestionnaireModel) : Call<String>

    @GET("/api/{phoneNumber}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getSerialNumber(@Path("phoneNumber") phoneNumber: String) : Call<String>

    @GET("/api/questionnaire/{phoneNumber}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getQuestionnaires(@Path("phoneNumber") phoneNumber: String) : Call<List<QuestionnaireModel>>

    companion object {
        private const val BASE_URL = "http://13.125.1.150:8080"

        fun create(): ApiService {
            val gson : Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }

}