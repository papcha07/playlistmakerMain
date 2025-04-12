package com.example.media.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(version = 2, entities = [TrackEntity::class , PlaylistEntity::class], exportSchema = true)
abstract class TrackDataBase : RoomDatabase(){
    abstract fun trackDao() : TrackDao
    abstract fun playListDao() : PlayListDao
}