package com.example.media.domain.api

import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun addPlayList(playList: PlayList)
    fun getAllPlayList() : Flow<List<PlayList>>
}