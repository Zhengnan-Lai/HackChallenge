package com.example.mememeet

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView:ImageView=itemView.findViewById(R.id.postView)
        val textView:TextView=itemView.findViewById(R.id.postText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.post_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=posts[position]
        holder.imageView.setImageURI(Uri.parse(post.image))
        for(i in post.words.indices){
            holder.textView.text = ""+holder.textView.text+" "+post.words[i]
        }
        val context=holder.itemView.context
        holder.itemView.setOnClickListener{
            val postIntent= Intent(context,PostActivity::class.java).apply{
                putExtra("Image",post.image)
            }
            context.startActivity(postIntent)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}