package com.example.media.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Query("SELECT * FROM track_table")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT id FROM track_table")
    suspend fun getFavoriteTracksId(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackEntity): Long

    @Delete
    suspend fun deleteTrack(track: TrackEntity): Int

}