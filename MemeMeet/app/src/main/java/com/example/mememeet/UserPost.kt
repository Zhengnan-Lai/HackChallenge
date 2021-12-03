package com.example.mememeet

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPost(
    val id:Int,
    val name:String,
    val posts: List<Post>
)
