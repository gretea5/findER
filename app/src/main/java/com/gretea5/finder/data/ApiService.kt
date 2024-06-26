package com.gretea5.finder.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gretea5.finder.BuildConfig
import com.gretea5.finder.data.model.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/api/account/signup")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun signUp(@Body authData: AuthData) : Call<String>

    @POST("/api/account/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun login(@Body authData: AuthData) : Call<String>

    @POST("/api/questionnaire")
    fun writeQuestionnaire(@Body questionnaireModel: QuestionnaireModel) : Call<String>

    @POST("/api/account/link")
    fun linkQuestionnaire(@Body questionnaireLinkModel: QuestionnaireLinkModel) : Call<String>

    @POST("/api/account/unlink")
    fun unLinkQuestionnaire(@Body questionnaireUnlinkModel: QuestionnaireUnlinkModel) : Call<String>

    @GET("/api/account/serialNumber/{phoneNumber}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getSerialNumber(@Path("phoneNumber") phoneNumber: String) : Call<String>

    @GET("/api/questionnaire/{phoneNumber}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getQuestionnaires(@Path("phoneNumber") phoneNumber: String) : Call<List<QuestionnaireModel>>

    @DELETE("/api/questionnaire/{phoneNumber}")
    fun deleteQuestionnaire(@Path("phoneNumber") phoneNumber: String) : Call<String>

    @PATCH("/api/questionnaire")
    fun updateQuestionnaire(@Body questionnaireModel: QuestionnaireModel) : Call<String>

    @GET("/api/er/location")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getERAll() : Call<List<LocationResponse>>

    @GET("/api/er/preview/{hpID}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getERPreview(
        @Path("hpID") hpID: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ) : Call<ERPreview>

    @GET("/api/er/detailView/{hpID}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getERDetailData(
        @Path("hpID") hpID: String,
        @Query("lat") lat : Double,
        @Query("lon") lon : Double
    ) : Call<ERDetail>

    companion object {
        private const val BASE_URL = BuildConfig.SERVER_URL

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