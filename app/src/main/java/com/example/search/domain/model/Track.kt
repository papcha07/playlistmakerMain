package com.example.search.domain.model

data class Track(val trackId: String,
            val trackName: String,
            val artistName: String,
            val trackTimeMillis: String,
            val artworkUrl100: String,
            val collectionName: String,
            val releaseDate: String?,
            val primaryGenreName: String,
            val country: String,
            val previewUrl: String?,
            var isFavorite: Boolean = false
){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}

