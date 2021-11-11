package com.example.project4

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.io.File


class SongActivity: AppCompatActivity(){

    private lateinit var imageView: ImageView
    private lateinit var songName: EditText
    private lateinit var songArtist: EditText
    private lateinit var songAlbum: EditText
    private lateinit var button: Button
    private lateinit var share: Button
    private lateinit var newImage: String
    val REQUEST_CODE=100


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        imageView=findViewById(R.id.imageView)
        songName=findViewById(R.id.textName)
        songArtist=findViewById(R.id.textArtist)
        songAlbum=findViewById(R.id.textAlbum)
        button=findViewById(R.id.button)
        share=findViewById(R.id.shareButton)

        val name=intent.extras?.getString("name")
        val artist=intent.extras?.getString("artist")
        val album=intent.extras?.getString("album")
        val position=intent.extras?.getInt("position")
        val image=intent.extras?.getString("image")

        if(name!=null) songName.setText(name)
        if(artist!=null) songArtist.setText(artist)
        if(album!=null) songAlbum.setText(album)
        if(image!=null) imageView.setImageURI(Uri.parse(image))
        if (image != null) {
            newImage=image
        }

        button.setOnClickListener {
            val mainIntent= Intent(this,MainActivity::class.java).apply{
                putExtra("position", position)
                putExtra("name",""+songName.text)
                putExtra("artist",""+songArtist.text)
                putExtra("album",""+songAlbum.text)
                putExtra("image",newImage)
            }
            startActivity(mainIntent)
        }

        share.setOnClickListener {
            val intent=Intent()
            val info="Song Name:"+songName.text+"\nArtist(s):"+songArtist.text+"\nAlbum:"+songAlbum.text
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, info)
            intent.type="text/plain"
            startActivity(intent)
        }

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_CODE)
                } else {
                    val snackbar =
                        Snackbar.make(imageView, "File access is not granted", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    snackbar.setAction("RETRY"){
                        snackbar.dismiss()
                    }
                }
            }

        imageView.setOnClickListener{
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {

                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_CODE)

                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    val snackbar =
                        Snackbar.make(imageView, "File access is not granted", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    snackbar.setAction("RETRY"){
                        snackbar.dismiss()
                    }
            }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imageView.setImageURI(data?.data)
            newImage=(data?.data).toString()
        }
    }
}