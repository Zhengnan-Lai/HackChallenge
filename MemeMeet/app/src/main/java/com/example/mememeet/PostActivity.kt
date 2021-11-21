package com.example.mememeet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostActivity : AppCompatActivity() {
    private lateinit var imageView:ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentButton: Button
    private lateinit var homeButton: Button
    private lateinit var tagButton: Button
    private lateinit var captionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        imageView=findViewById(R.id.postImageView)
        recyclerView=findViewById(R.id.commentRecView)
        commentButton=findViewById(R.id.commentButton)
        homeButton=findViewById(R.id.homeButton)
        tagButton=findViewById(R.id.tagButton)
        captionText=findViewById(R.id.postCaption)

        val image=intent.extras?.getString("image")
        val userId=intent.extras?.getInt("userId")
        val postId=intent.extras?.getInt("postId")
        val caption=intent.extras?.getString("caption")
        val tag=intent.extras?.getString("tag")

        imageView.setImageURI(Uri.parse(image))
        captionText.text=caption
        tagButton.text=tag

        homeButton.setOnClickListener {
            val homeIntent= Intent(this,MainActivity::class.java)
            startActivity(homeIntent)
        }

        tagButton.setOnClickListener {
            //TODO direct to tag page
        }

        commentButton.setOnClickListener {
            //TODO Post a comment
        }
    }
}