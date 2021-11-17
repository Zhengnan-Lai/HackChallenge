package com.example.mememeet

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MemeActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        imageView=findViewById(R.id.memeEditView)

        val image=intent.extras?.getString("Image")
        imageView.setImageURI(Uri.parse(image))

    }
}