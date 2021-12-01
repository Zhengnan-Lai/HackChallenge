package com.example.mememeet

import android.R.attr
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import android.R.attr.bitmap

import android.R.drawable
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import android.os.Environment
import java.lang.Exception
import android.content.Intent
import android.provider.MediaStore
import android.R.attr.foreground
import android.graphics.*
import android.os.Build
import android.text.TextPaint
import androidx.annotation.RequiresApi
import android.R.attr.foreground
import android.app.Activity

import android.graphics.Bitmap





class MemeActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var postButton: Button
    private lateinit var memeText: TextView
    private lateinit var postWords: TextView

    private lateinit var image:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        imageView=findViewById(R.id.memeEditView)
        saveButton=findViewById(R.id.saveMemeButton)
        postButton=findViewById(R.id.postMemeButton)
        memeText=findViewById(R.id.memeBottomTexts)
        postWords=findViewById(R.id.memePostWords)

        //Load an image
        val imageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(imageIntent, REQUEST_CODE)

        saveButton.setOnClickListener {
            //TODO add a status bar
            imageView.setImageURI(Uri.parse(image))
            imageView.buildDrawingCache()
            val background=imageView.drawingCache
            val newImage=combineImage(background, ""+memeText.text)
            imageView.setImageBitmap(newImage)
            saveImage(newImage)
        }

        postButton.setOnClickListener {
            //TODO Post the meme and words (Remember to add a status bar)
            val homeIntent=Intent(this,MainActivity::class.java)
            startActivity(homeIntent)
        }

        memeText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                imageView.setImageURI(Uri.parse(image))
                imageView.buildDrawingCache()
                val background=imageView.drawingCache
                val newImage=combineImage(background, ""+memeText.text)
                imageView.setImageBitmap(newImage)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    //Method to load an image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            image= data?.data.toString()
            imageView.setImageURI(Uri.parse(image))
        }
    }

    // Method to save an image to internal storage
    private fun saveImage(image: Bitmap){
        imageView.setImageBitmap(image)
        MediaStore.Images.Media.insertImage(
            contentResolver,
            image,
            "Image",
            "Image"
        )
    }

    // Method to put texts under the image
    private fun combineImage(image: Bitmap, text: String): Bitmap {
        var width = 1024
        var height = 1024

        val ret: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val comboImage = Canvas(ret)
        val background = Bitmap.createScaledBitmap(image, width, height, true)
        comboImage.drawBitmap(background, 0F, 0F, null)

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        textPaint.style = Paint.Style.FILL
        textPaint.color = Color.WHITE
        textPaint.textSize = 100f
        val x=(comboImage.width-textPaint.measureText(text))/2
        val y=1000f
        comboImage.drawText(text, x.toFloat(), y, textPaint)

        return ret
    }
}