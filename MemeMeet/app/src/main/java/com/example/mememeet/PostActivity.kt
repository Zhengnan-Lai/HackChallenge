package com.example.mememeet

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.lang.Exception

class PostActivity : AppCompatActivity() {
    private lateinit var imageView:ImageView
    private lateinit var homeButton: Button
    private lateinit var tagButton: Button
    private lateinit var captionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        imageView=findViewById(R.id.postImageView)
        homeButton=findViewById(R.id.profileButton)
        tagButton=findViewById(R.id.tagButton)
        captionText=findViewById(R.id.postCaption)

        val image=StringToBitMap(intent.extras?.getString("image"))
        val caption=intent.extras?.getString("caption")
        val tag=intent.extras?.getString("tag")
        val tagId=

        imageView.setImageBitmap(image)
        captionText.text=caption
        tagButton.text=tag

        homeButton.setOnClickListener {
            val profileIntent= Intent(this,MainActivity::class.java)
        }

        tagButton.setOnClickListener {
            val tagIntent=Intent(this,TagActivity::class.java)
            startActivity(tagIntent)
        }
        
    }

    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun StringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }
}