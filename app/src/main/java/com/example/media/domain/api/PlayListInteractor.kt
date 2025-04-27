package com.example.media.domain.api

import com.example.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun addPlayList(playList: PlayList)
    fun getAllPlayList() : Flow<List<PlayList>>
    suspend fun addTrackInPlayList(track: Track, playList: PlayList) : Boolean
}