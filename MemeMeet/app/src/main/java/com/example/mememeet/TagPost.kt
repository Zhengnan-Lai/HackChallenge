package com.example.mememeet

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagPost(val id:Int, val tag: String, val posts: List<Post>)
