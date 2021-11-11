package com.example.hw6

class Repository private constructor(){
    companion object{
        val noteList= mutableListOf<Note>()
    }
}