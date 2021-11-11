package com.example.project4

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(private val songs: List<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>(){

    class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView =itemView.findViewById(R.id.songName)
        val artist: TextView =itemView.findViewById(R.id.songArtist)
        val album: TextView =itemView.findViewById(R.id.songAlbum)
        val image: ImageView=itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.song_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song=songs[position]
        holder.name.text=song.name
        holder.artist.text=song.artist
        holder.album.text=song.album
        holder.image.setImageURI(Uri.parse(song.image))
        val context=holder.itemView.context
        holder.itemView.setOnClickListener{
            val songIntent= Intent(context,SongActivity::class.java).apply{
                putExtra("position", position)
                putExtra("name",song.name)
                putExtra("artist",song.artist)
                putExtra("album",song.album)
                putExtra("image",song.image)
            }
            context.startActivity(songIntent)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}