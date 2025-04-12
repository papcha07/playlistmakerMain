package com.example.media.data

import com.example.media.db.TrackDataBase
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListRepository
import kotlinx.coroutines.Dispatchers

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
}