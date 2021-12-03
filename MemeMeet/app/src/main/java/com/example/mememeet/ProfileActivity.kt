package com.example.mememeet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException

class ProfileActivity : AppCompatActivity() {
    private lateinit var homeButton: Button
    private lateinit var nameText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    private val posts: MutableList<Post> = mutableListOf()
    private val client = OkHttpClient()
    private val moshi= Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val userPostJsonAdapter: JsonAdapter<UserPost> = moshi.adapter(UserPost::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        homeButton=findViewById(R.id.button)
        nameText=findViewById(R.id.userNameText)
        recyclerView=findViewById(R.id.recyclerview)

        homeButton.setOnClickListener {
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }

        populateUserList()
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    private fun populateUserList() {
        val id= intent.extras?.getInt("user")!!
        val requestGet = Request.Builder().url(BASE_URL + "user/"+id.toString()+"/").build()
        client.newCall(requestGet).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        throw IOException("Network call unsuccessful")
                    }
                    val postList = userPostJsonAdapter.fromJson(response.body!!.string())!!
                    for (post in postList.posts) {
                        posts.add(Post(post.id, post.caption, post.image,User(postList.id,postList.name),post.tag))
                    }
                    adapter = PostAdapter(posts)
                    runOnUiThread {
                        nameText.text=postList.name
                        recyclerView.adapter = adapter
                    }
                }
            }
        })
    }
}