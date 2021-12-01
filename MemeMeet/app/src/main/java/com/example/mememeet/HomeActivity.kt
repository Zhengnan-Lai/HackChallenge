package com.example.mememeet

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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


private const val NUM_FRAGMENTS=2
class MainActivity : AppCompatActivity() {
    private lateinit var profileButton: Button
    private lateinit var searchText: AutoCompleteTextView
    private lateinit var recyclerView: RecyclerView

    private val postList= mutableListOf<Post>()
    private val tags = arrayOf("Cats","Doge")

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