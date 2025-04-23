package com.example.media.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")

data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name: String,
    val description : String,
    var path: String? = null,
    val trackList : String,
    val trackCount : Int
)