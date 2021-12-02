package com.example.mememeet

import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

const val REQUEST_CODE=1000

class TagActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeButton: Button
    private lateinit var addImageButton: Button

    private val postList= mutableListOf<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        recyclerView=findViewById(R.id.tagRecyclerView)
        homeButton=findViewById(R.id.tagToHomeButton)
        addImageButton=findViewById(R.id.addImageButton)

        homeButton.setOnClickListener {
            val homeIntent= Intent(this,MainActivity::class.java)
            startActivity(homeIntent)
        }

        addImageButton.setOnClickListener {
            val memeIntent=Intent(this,MemeActivity::class.java)
            startActivity(memeIntent)
        }

//        val image = R.drawable.irelia
//        val image1 = R.drawable.sera
//        val list= mutableListOf<String>()
//        postList.add(Post(1,1,"LOL", getURI(image), "Irelia",list))
//        postList.add(Post(1, 2, "LOL", getURI(image1), "Seraphine", list))

        val adapter=PostAdapter(postList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }



    //get URI of an image from drawable
    fun getURI(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + resourceId)
            .toString()
    }
}