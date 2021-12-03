package com.example.mememeet

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.ContentResolver

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.util.Base64
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.lang.Exception


class PostAdapter(private val posts: List<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val postImage:ImageView=itemView.findViewById(R.id.postImage)
        val userId:TextView=itemView.findViewById(R.id.userIdText)
        val tagButton: Button =itemView.findViewById(R.id.tagButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.post_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=posts[position]
        holder.postImage.setImageBitmap(StringToBitMap(post.image))
        holder.userId.text = post.user?.name.toString()
        holder.tagButton.text= post.tag?.tag.toString()
        val context=holder.itemView.context
        holder.tagButton.setOnClickListener {
            val tagIntent=Intent(context, TagActivity::class.java)
            if(holder.tagButton.text=="cat")
                tagIntent.putExtra("tag",1)
            else
                tagIntent.putExtra("tag",2)
            context.startActivity(tagIntent)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun StringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }
}