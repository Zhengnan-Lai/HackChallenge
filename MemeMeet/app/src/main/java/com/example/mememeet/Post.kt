package com.example.mememeet

import android.graphics.Bitmap
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Post(
    val id: Int,
    val caption: String,
    val image: String,
    val user: User,
    val tag: Tag,
    val comment: List<String>?=null,
)

