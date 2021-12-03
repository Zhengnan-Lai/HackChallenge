package com.example.mememeet

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendPost(val caption: String, val image: String, val user_id: Int)
