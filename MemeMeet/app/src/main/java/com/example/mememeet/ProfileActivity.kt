package com.example.mememeet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {
    private lateinit var homeButton: Button
    private lateinit var nameText: TextView
    private lateinit var recyclerView: RecyclerView

    private val postList= mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        homeButton=findViewById(R.id.button)
        nameText=findViewById(R.id.userName)
        recyclerView=findViewById(R.id.recyclerview)

        nameText.text="Santiago"

        homeButton.setOnClickListener {
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }

        val image = R.drawable.irelia
        val image1 = R.drawable.sera
        val list= mutableListOf<String>()
        postList.add(Post(1,1,"LOL", getURI(image), "Irelia",list))
        postList.add(Post(1, 2, "LOL", getURI(image1), "Seraphine", list))

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