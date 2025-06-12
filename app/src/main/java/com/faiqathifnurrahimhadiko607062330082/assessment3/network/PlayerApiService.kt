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
import retrofit2.http.Query

// Base URL dari API
private const val BASE_URL = "https://lfc-player-api.vercel.app/"

// Inisialisasi Moshi untuk parsing JSON
// KotlinJsonAdapterFactory memungkinkan Moshi bekerja dengan data class Kotlin
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Inisialisasi Retrofit
// MoshiConverterFactory digunakan untuk mengkonversi JSON response menjadi objek Kotlin
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Interface yang mendefinisikan endpoint API
interface LfcApiService {
    /**
     * Mengambil daftar semua pemain Liverpool.
     * Response berupa List dari objek Player.
     */
    @GET("players") // Endpoint untuk mendapatkan semua pemain
    suspend fun getPlayers(): List<Player>

    // Contoh metode POST (TIDAK AKAN BERFUNGSI DENGAN API SAAT INI)
    // Mengasumsikan endpoint "players" juga digunakan untuk POST,
    // dan memerlukan otorisasi serta data pemain dalam format multipart.
    @Multipart
    @POST("players") // Atau endpoint spesifik untuk POST jika berbeda
    suspend fun addPlayer(
        @Header("Authorization") authToken: String, // Contoh otorisasi
        @Part("name") name: RequestBody,
        @Part("position") position: RequestBody,
        @Part("number") number: RequestBody, // Angka bisa dikirim sebagai RequestBody
        @Part("nationality") nationality: RequestBody,
        @Part photo: MultipartBody.Part? = null // Foto pemain, opsional
    ): OpStatus

    // Contoh metode DELETE (TIDAK AKAN BERFUNGSI DENGAN API SAAT INI)
    // Mengasumsikan endpoint "players" digunakan untuk DELETE dengan ID pemain sebagai query parameter.
    @DELETE("players") // Atau endpoint spesifik untuk DELETE jika berbeda
    suspend fun deletePlayer(
        @Header("Authorization") authToken: String, // Contoh otorisasi
        @Query("id") playerId: String // ID pemain yang akan dihapus
    ): OpStatus
}

// Objek singleton untuk menyediakan instance dari LfcApiService
object LfcApi {
    val service: LfcApiService by lazy {
        retrofit.create(LfcApiService::class.java)
    }
}

// (Opsional) Enum untuk status pemanggilan API, bisa digunakan di ViewModel
enum class LfcApiStatus { LOADING, SUCCESS, FAILED }