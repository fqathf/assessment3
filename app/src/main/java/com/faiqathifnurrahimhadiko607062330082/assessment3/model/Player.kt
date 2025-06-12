package com.faiqathifnurrahimhadiko607062330082.assessment3.model

import com.squareup.moshi.Json

data class Player(
    @Json(name = "id")
    val id: String, // Berdasarkan contoh, ID adalah Integer

    @Json(name = "name")
    val name: String,

    @Json(name = "position")
    val position: String,

    @Json(name = "number")
    val number: Int, // Berdasarkan contoh, number adalah Integer

    @Json(name = "nationality")
    val nationality: String,

    @Json(name = "photo") // Mengganti "image" dengan "photo" agar sesuai dengan JSON
    val image: String // Nama properti tetap "image" agar konsisten dengan penggunaan di UI Anda,
    // tetapi dipetakan dari field "photo" di JSON.
    // Jika Anda ingin nama properti juga "photo", ubah menjadi: val photo: String
)