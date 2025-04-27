package com.example.media.domain.api

data class PlayList(
    val id : Int,
    val name: String,
    val description : String,
    var path: String? = null,
    var trackList : String,
    var trackCount : Int
)
