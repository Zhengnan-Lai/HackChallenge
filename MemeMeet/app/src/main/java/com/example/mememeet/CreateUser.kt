package com.example.mememeet

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateUser(val name:String)
