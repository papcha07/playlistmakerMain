package com.example.media.data

import androidx.room.util.foreignKeyCheck
import com.example.media.db.PlaylistEntity
import com.example.media.db.TrackDataBase
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val db: TrackDataBase,
    private val playListDbConverter : PlaylistDbConverter
) : PlayListRepository {

    override suspend fun addPlayList(playlist: PlayList) {
        with(Dispatchers.IO){
            val playlistEntity = playListDbConverter.map(playlist)
            db.playListDao().addPlaylist(playlistEntity)
        }
    }

    override fun getAllPlayLists(): Flow<List<PlayList>> = flow{
        val entityList = db.playListDao().getAllPlayLists()
        val convertedList = convertToPlayList(entityList)
        emit(convertedList)
    }

    private fun convertToPlayList(list: List<PlaylistEntity>) : List<PlayList>{
        return list.map {
            track ->
            playListDbConverter.map(track)
        }
    }
}