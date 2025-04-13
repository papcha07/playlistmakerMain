package com.example.media.domain.api

import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun addPlayList(playlist: PlayList)
    fun getAllPlayLists() : Flow<List<PlayList>>
}