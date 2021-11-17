package com.example.mememeet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemeAdapter(private val memes: List<Meme>): RecyclerView.Adapter<MemeAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView:ImageView=itemView.findViewById(R.id.memeView)
        val textView:TextView=itemView.findViewById(R.id.tagText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.meme_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meme=memes[position]
        holder.imageView.setImageBitmap(meme.image)
        for(i in meme.tags.indices){
            holder.textView.setText(""+holder.textView.text+" "+meme.tags[i])
        }
        holder.itemView.setOnClickListener{
            //TODO: Direct to another acitivity
        }
    }

    override fun getItemCount(): Int {
        return memes.size
    }
}