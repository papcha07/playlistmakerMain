package com.example.media.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlayLists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :playListId")
    suspend fun getPlayListById(playListId: Int): PlaylistEntity

    @Update
    suspend fun updatePlayList(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE id == :id")
    suspend fun deletePlayList(id: Int)

}
