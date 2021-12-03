package com.example.mememeet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private lateinit var userText:EditText
    private lateinit var loginButton:Button
    private lateinit var signupButton:Button
    private lateinit var postList:UserPost

    private val client = OkHttpClient()
    private val moshi= Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val createTagJsonAdapter: JsonAdapter<CreateTag> = moshi.adapter(CreateTag::class.java)
    private val createUserJsonAdapter: JsonAdapter<CreateUser> = moshi.adapter(CreateUser::class.java)
    private val userPostJsonAdapter: JsonAdapter<UserPost> = moshi.adapter(UserPost::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userText=findViewById(R.id.enterUserText)
        loginButton=findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val homeIntent= Intent(this,MainActivity::class.java)
            runBlocking {
                withContext(Dispatchers.IO) {
                    createUser(""+userText.text)
                    createTag("cat")
                    createTag("doge")
                }
            }
            homeIntent.putExtra("user",postList.id)
            startActivity(homeIntent)
        }

    }

    private fun createUser(user:String){
        val newUser=CreateUser(user)
        val requestPost = Request.Builder().url(BASE_URL + "user/")
            .post(createUserJsonAdapter.toJson(newUser).toRequestBody(("application/json; charset=utf-8").toMediaType())).build()
        client.newCall(requestPost).execute().use{
            if(!it.isSuccessful){
                throw IOException("Create user unsuccessful")
            }
            postList = userPostJsonAdapter.fromJson(it.body!!.string())!!
        }
    }

    private fun createTag(tag:String){
        val newTag=CreateTag(tag)
        val requestPost = Request.Builder().url(BASE_URL + "post/")
            .post(createTagJsonAdapter.toJson(newTag).toRequestBody(("application/json; charset=utf-8").toMediaType())).build()
        client.newCall(requestPost).execute().use{
            if(!it.isSuccessful){
                throw IOException("Create tag unsuccessful")
            }
        }
    }
}