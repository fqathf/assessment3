package com.faiqathifnurrahimhadiko607062330082.assessment3.network

import com.faiqathifnurrahimhadiko607062330082.assessment3.model.Player // Pastikan import model Player sudah benar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

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
    @GET("players") // Endpoint spesifik setelah base URL
    suspend fun getPlayers(): List<Player>
}

// Objek singleton untuk menyediakan instance dari LfcApiService
object LfcApi {
    val service: LfcApiService by lazy {
        retrofit.create(LfcApiService::class.java)
    }
}

// (Opsional) Enum untuk status pemanggilan API, bisa digunakan di ViewModel
enum class LfcApiStatus { LOADING, SUCCESS, FAILED }