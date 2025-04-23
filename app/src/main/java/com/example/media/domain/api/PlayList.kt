package com.example.media.domain.api

data class PlayList(
    val id : Int,
    val name: String,
    val description : String,
    var path: String? = null,
    val trackList : String,
    val trackCount : Int
)
