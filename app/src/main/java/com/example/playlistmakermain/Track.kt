package com.example.playlistmakermain

class Track(val trackId: String,
            val trackName: String,
            val artistName: String,
            val trackTimeMillis: String,
            val artworkUrl100: String,
            val collectionName: String,
            val releaseDate: String?,
            val primaryGenreName: String,
            val country: String,
            val previewUrl: String?){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}

