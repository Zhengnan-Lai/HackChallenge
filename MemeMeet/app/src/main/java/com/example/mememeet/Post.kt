package com.example.mememeet

import android.graphics.Bitmap

data class Post(val user_id: Int, val post_id: Int, val tag: String, val image: String, val caption: String, val comment: List<String>)
