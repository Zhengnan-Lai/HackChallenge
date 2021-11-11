package com.example.hw6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException

class LocalNotesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_notes)

        recyclerView=findViewById(R.id.localRecyclerview)
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        button=findViewById(R.id.backButton)

        populateNotesList()

        button.setOnClickListener {
            val cloudIntent= Intent(this, CloudNotesActivity::class.java)
            startActivity(cloudIntent)
        }
    }

    private fun populateNotesList(){
        adapter=NoteAdapter(Repository.noteList)
        recyclerView.adapter=adapter
    }
}
