package com.example.mememeet

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
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
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException

const val BASE_URL="https://mememeet.herokuapp.com/"

class MainActivity : AppCompatActivity() {
    private lateinit var profileButton: Button
    private lateinit var searchText: AutoCompleteTextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    private val tags = arrayOf("cat","doge")

    private val posts: MutableList<Post> = mutableListOf()
    private val client = OkHttpClient()
    private val moshi= Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val postJsonAdapter: JsonAdapter<Post> = moshi.adapter(Post::class.java)
    private val postsJsonAdapter: JsonAdapter<Posts> = moshi.adapter(Posts::class.java)
    private val postListType= Types.newParameterizedType(List::class.java, Post::class.java)
    private val postListJsonAdapter: JsonAdapter<List<Post>> = moshi.adapter(postListType)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileButton=findViewById(R.id.profileButton)
        searchText=findViewById(R.id.searchText)
        recyclerView=findViewById(R.id.postRecyclerView)

        profileButton.setOnClickListener {
            val mainActivityIntent = Intent(this, ProfileActivity::class.java)
            startActivity(mainActivityIntent)

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
            val intent=Intent(this, TagActivity::class.java)
            startActivity(intent)
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
                    val postList = postsJsonAdapter.fromJson(response.body!!.string())!!
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

    //get URI of an image from drawable
    fun getURI(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + resourceId)
            .toString()
    }
}