package com.faiqathifnurrahimhadiko607062330082.assessment3.network

import com.faiqathifnurrahimhadiko607062330082.assessment3.model.OpStatus
import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_URL = "https://apimobpromobil-production.up.railway.app/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface PlayerApiService {
    @GET("players")
    suspend fun getPlayers(
        @Header("Authorization") authToken: String
    ): List<Player>

    @Multipart
    @POST("players/store")
    suspend fun addPlayer(
        @Header("Authorization") authToken: String,
        @Part("nama") nama: RequestBody,
        @Part("posisi") posisi: RequestBody,
        @Part foto: MultipartBody.Part
    ): OpStatus

    @DELETE("players/{id}")
    suspend fun deletePlayer(
        @Header("Authorization") authToken: String,
        @Path("id") id: String
    ): OpStatus

    @Multipart
    @POST("players/{id}")
    suspend fun updatePlayer(
        @Header("Authorization") authToken: String,
        @Part("nama") name: RequestBody,
        @Part("posisi") position: RequestBody,
        @Part foto: MultipartBody.Part,
        @Path("id") id: String
    ): OpStatus
}

object PlayerApi {
    val service: PlayerApiService by lazy {
        retrofit.create(PlayerApiService::class.java)
    }

    fun getPlayerPhotoUrl(photoId: String): String {
        return "https://apimobpromobil-production.up.railway.app/storage/$photoId"
    }
}

enum class PlayerApiStatus { LOADING, SUCCESS, FAILED }