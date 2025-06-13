package com.faiqathifnurrahimhadiko607062330082.assessment3.model

import com.squareup.moshi.Json

data class Player(
    val id : String,
    val nama: String,
    val posisi: String,
    val foto: String,
    val Authorization: String?
)