package com.example.mememeet

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(val id: Int, val tag: String)
