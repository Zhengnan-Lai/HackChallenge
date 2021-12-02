package com.example.mememeet

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(val id: Int, val name: String)
