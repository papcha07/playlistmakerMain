package com.example.media.domain.api

data class PlayList(
    val id : Int,
    val name: String,
    val description : String,
    var path: String,
    val trackList : String,
    val trackCount : Int
)
