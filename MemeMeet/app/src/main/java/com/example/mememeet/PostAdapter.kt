package com.example.mememeet

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val postImage:ImageView=itemView.findViewById(R.id.postImage)
        val userId:TextView=itemView.findViewById(R.id.userIdText)
        val commentButton: Button =itemView.findViewById(R.id.commentButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.post_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=posts[position]
        holder.postImage.setImageURI(Uri.parse(post.image))
        holder.userId.text = post.user_id.toString()
        holder.commentButton.isEnabled = false
        val context=holder.itemView.context
        holder.itemView.setOnClickListener{
            val postIntent= Intent(context,PostActivity::class.java).apply{
                putExtra("image",post.image)
                putExtra("userId",post.user_id)
                putExtra("postId",post.post_id)
                putExtra("caption",post.caption)
                putExtra("tag",post.tag)
            }
            context.startActivity(postIntent)
        }


    }

    override fun getItemCount(): Int {
        return posts.size
    }
}