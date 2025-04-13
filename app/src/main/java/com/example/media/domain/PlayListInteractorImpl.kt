package com.example.media.domain

import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListInteractor
import com.example.media.domain.api.PlayListRepository
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(

    private val playListRepository: PlayListRepository
) : PlayListInteractor {

    override suspend fun addPlayList(playList: PlayList) {
        playListRepository.addPlayList(playList)
    }

    override fun getAllPlayList(): Flow<List<PlayList>> {
        return playListRepository.getAllPlayLists()
    }
}