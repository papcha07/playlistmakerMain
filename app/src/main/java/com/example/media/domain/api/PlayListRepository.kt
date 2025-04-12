package com.example.media.domain.api

interface PlayListRepository {
    suspend fun addPlayList(playlist: PlayList)
}