package com.example.mememeet

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception

class PostActivity : AppCompatActivity() {
    private lateinit var imageView:ImageView
    private lateinit var homeButton: ImageButton
    private lateinit var tagButton: Button
    private lateinit var captionText: TextView
    private lateinit var post:Post

    private val client = OkHttpClient()
    private val moshi= Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val postJsonAdapter: JsonAdapter<Post> = moshi.adapter(Post::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        imageView=findViewById(R.id.postImageView)
        homeButton=findViewById(R.id.postToProfileButton)
        tagButton=findViewById(R.id.tagButton)
        captionText=findViewById(R.id.postCaption)

        val postId=intent.extras?.getInt("postId")
        val id=intent.extras?.getInt("user")

        populatePost(id!!,postId!!)

        homeButton.setOnClickListener {
            val profileIntent= Intent(this,ProfileActivity::class.java)
            profileIntent.putExtra("user",id)
            profileIntent.putExtra("userName",post.user?.name)
            startActivity(profileIntent)
        }

        tagButton.setOnClickListener {
            val tagIntent=Intent(this,TagActivity::class.java)
            tagIntent.putExtra("tagName",post.tag?.tag)
            tagIntent.putExtra("tag",post.tag?.id)
            tagIntent.putExtra("user",id)
            tagIntent.putExtra("userName",post.user?.name)
            startActivity(tagIntent)
        }

    }

    private fun populatePost(id:Int, postId: Int) {
        val requestGet = Request.Builder().url(BASE_URL+"user/"+id.toString()+"/post/"+postId.toString()+"/").build()
        client.newCall(requestGet).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        throw IOException("Network call unsuccessful")
                    }
                    post=postJsonAdapter.fromJson(response.body!!.string())!!
                    runOnUiThread {
                        imageView.setImageBitmap(StringToBitMap(post.image))
                        captionText.text=post.caption
                        tagButton.text="#"+post.tag?.tag
                    }
                }
            }
        })
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