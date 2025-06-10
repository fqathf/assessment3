package com.faiqathifnurrahimhadiko607062330082.assessment3.model

import com.squareup.moshi.Json

data class Player(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "position")
    val position: String,

    @Json(name = "number")
    val number: Int,

    @Json(name = "nationality")
    val nationality: String,

    @Json(name = "photo")
    val photoUrl: String // Menggunakan nama photoUrl agar lebih deskriptif di Kotlin
)
