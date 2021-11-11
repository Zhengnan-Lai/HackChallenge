package com.example.hw6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class EditNoteActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var editBody: EditText
    private lateinit var button: Button

    private val client = OkHttpClient()
    private val moshi= Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val noteJsonAdapter: JsonAdapter<Note> = moshi.adapter(Note::class.java)
    private val noteListType= Types.newParameterizedType(List::class.java, Note::class.java)
    private val noteListJsonAdapter: JsonAdapter<List<Note>> = moshi.adapter(noteListType)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_node)

        editTitle=findViewById(R.id.editTitle)
        editBody=findViewById(R.id.editBody)
        button=findViewById(R.id.editButton)

        val position=intent.extras?.getInt("position")
        val title=intent.extras?.getString("title")
        val body=intent.extras?.getString("body")
        val user=intent.extras?.getString("user")
        val timeStamp=intent.extras?.getString("timeStamp")

        editTitle.setText(title)
        editBody.setText(body)

        button.setOnClickListener {
            val cloudIntent= Intent(this, CloudNotesActivity::class.java)
            if(position!=null){
                for(note in Repository.noteList){
                    if(note.id==position){
                        Repository.noteList.remove(note)
                        break
                    }
                }
                Repository.noteList.add(Note(""+editTitle.text,""+editBody.text,user,timeStamp!!,position!!))
                runBlocking {
                    withContext(Dispatchers.IO) {
                        updateNote(""+editTitle.text,""+editBody.text, position)
                    }
                }
            }
            else{
                runBlocking {
                    withContext(Dispatchers.IO) {
                        createNote(""+editTitle.text, ""+editBody.text)
                    }
                }

            }
            startActivity(cloudIntent)
        }
    }
    private fun updateNote(title: String, body: String, id: Int){
        val newNote=Note(title, body, "zl345")
        val requestPost = Request.Builder().url(BASE_URL + "posts/"+id.toString()+"/")
            .post(noteJsonAdapter.toJson(newNote).toRequestBody(("application/json; charset=utf-8").toMediaType())).build()
        client.newCall(requestPost).execute().use{
            if(!it.isSuccessful){
                throw IOException("Post unsuccessful")
            }
        }
    }

    private fun createNote(title: String, body: String){
        val newNote=Note(title, body, "zl345")
        val requestPost = Request.Builder().url(BASE_URL + "posts/")
            .post(noteJsonAdapter.toJson(newNote).toRequestBody(("application/json; charset=utf-8").toMediaType())).build()
        client.newCall(requestPost).execute().use{
            if(!it.isSuccessful){
                throw IOException("Post unsuccessful")
            }
        }
    }
}

