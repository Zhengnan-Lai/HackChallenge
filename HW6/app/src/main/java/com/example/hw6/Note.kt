package com.example.hw6

import com.squareup.moshi.JsonClass
import java.time.Instant


@JsonClass(generateAdapter = true)
data class Note(
    val title: String?="",
    val body: String,
    val poster: String?="",
    val timestamp: String?="",
    val id: Int?=-1
)
