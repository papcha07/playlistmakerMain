package com.example.media.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)
}