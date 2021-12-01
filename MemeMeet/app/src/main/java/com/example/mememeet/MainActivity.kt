package com.example.mememeet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val NUM_FRAGMENTS=2
class MainActivity : AppCompatActivity() {
    private lateinit var profileButton: Button
    private lateinit var searchText: EditText
    private lateinit var recyclerView: RecyclerView

    private val postList= mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileButton=findViewById(R.id.profileButton)
        searchText=findViewById(R.id.searchText)
        recyclerView=findViewById(R.id.postRecyclerView)

        val image = R.drawable.irelia
        val image1 = R.drawable.sera
        val image2 = R.drawable.cam
        val list= mutableListOf<String>()
        postList.add(Post(1,1,"LOL", getURI(image), "Irelia",list))
        postList.add(Post(1, 2, "LOL", getURI(image1), "Seraphine", list))
        postList.add(Post(1, 3, "LOL", getURI(image2), "Camille", list))


        profileButton.setOnClickListener {
            val mainActivityIntent = Intent(this, ProfileActivity::class.java)
            startActivity(mainActivityIntent)

        }

        searchText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO Add search function
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


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