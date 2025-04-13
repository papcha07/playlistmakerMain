package com.example.media.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlayLists() : List<PlaylistEntity>
}