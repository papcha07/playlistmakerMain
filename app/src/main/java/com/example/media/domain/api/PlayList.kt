package com.example.media.domain.api

data class PlayList(
    val id : Int,
    val name: String,
    val description : String,
    val path: String,
    val tracksList : MutableList<Int>,
    val trackCount : Int
)
