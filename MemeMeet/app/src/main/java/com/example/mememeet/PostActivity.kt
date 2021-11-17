package com.example.mememeet

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PostActivity : AppCompatActivity() {
    private lateinit var imageView:ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var postButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        imageView=findViewById(R.id.postImageView)
        recyclerView=findViewById(R.id.commentRecView)
        postButton=findViewById(R.id.commentButton)

        val image=intent.extras?.getString("Image")
        imageView.setImageURI(Uri.parse(image))
    }
}