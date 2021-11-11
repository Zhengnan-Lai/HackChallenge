package com.example.hw6

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val notes: List<Note>): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView=itemView.findViewById(R.id.noteTitle)
        val body: TextView=itemView.findViewById(R.id.noteBody)
        val user: TextView=itemView.findViewById(R.id.userName)
        val time: TextView=itemView.findViewById(R.id.timeStamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.note_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note=notes[position]
        holder.title.text=note.title
        holder.body.text=note.body
        holder.user.text=note.poster
        holder.time.text= note.timestamp
        val context=holder.itemView.context
        holder.itemView.setOnClickListener {
            if(context !is LocalNotesActivity) {
                val editIntent= Intent(context, EditNoteActivity::class.java).apply{
                    putExtra("position",position)
                    putExtra("title", note.title)
                    putExtra("body",note.body)
                    putExtra("user",note.poster)
                    putExtra("timeStamp",note.timestamp)
                }
                context.startActivity(editIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}