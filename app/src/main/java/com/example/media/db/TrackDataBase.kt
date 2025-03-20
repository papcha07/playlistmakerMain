package com.example.media.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(version = 1, entities = [TrackEntity::class], exportSchema = true)
abstract class TrackDataBase : RoomDatabase(){
    abstract fun trackDao() : TrackDao
}