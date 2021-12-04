package com.example.mememeet

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.view.View.OnFocusChangeListener
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

const val BASE_URL="https://mememeet.herokuapp.com/"

class MainActivity : AppCompatActivity() {
    private lateinit var profileButton: ImageButton
    private lateinit var searchText: AutoCompleteTextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var postList: Posts

    private val tags = arrayOf("cat","doge")
    private val posts: MutableList<Post> = mutableListOf()
    private val client = OkHttpClient()
    private val moshi= Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val createTagJsonAdapter: JsonAdapter<CreateTag> = moshi.adapter(CreateTag::class.java)
    private val createUserJsonAdapter: JsonAdapter<CreateUser> = moshi.adapter(CreateUser::class.java)
    private val postsJsonAdapter: JsonAdapter<Posts> = moshi.adapter(Posts::class.java)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val id=intent.extras?.getInt("user")
        val userName=intent.extras?.getString("userName")

        profileButton=findViewById(R.id.profileButton)
        searchText=findViewById(R.id.searchText)
        recyclerView=findViewById(R.id.postRecyclerView)

        profileButton.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java)
            profileIntent.putExtra("user",id)
            profileIntent.putExtra("userName",userName)
            startActivity(profileIntent)
        }

        val searchAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            android.R.layout.select_dialog_item, tags)
        searchText.threshold = 1
        searchText.setAdapter(searchAdapter)
        searchText.setOnTouchListener { _, _ ->
            searchText.showDropDown()
            false
        }

        searchText.setOnItemClickListener { adapterView, view, i, l ->
            val tagIntent=Intent(this, TagActivity::class.java)
            if(adapterView.getItemAtPosition(i) as String=="cat"){
                tagIntent.putExtra("tag",1)
                tagIntent.putExtra("tagName","cat")
            }

            else{
                tagIntent.putExtra("tag",2)
                tagIntent.putExtra("tagName","doge")
            }
            tagIntent.putExtra("user",id)
            tagIntent.putExtra("userName",userName)
            startActivity(tagIntent)
        }

        populatePostsList()
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    private fun populatePostsList() {
        val requestGet = Request.Builder().url(BASE_URL + "post/").build()
        client.newCall(requestGet).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        throw IOException("Network call unsuccessful")
                    }
                    postList = postsJsonAdapter.fromJson(response.body!!.string())!!
                    for (post in postList.posts) {
                        posts.add(post)
                    }
                    adapter = PostAdapter(posts)

                    runOnUiThread {
                        recyclerView.adapter = adapter
                    }
                }
            }
        })
    }
}