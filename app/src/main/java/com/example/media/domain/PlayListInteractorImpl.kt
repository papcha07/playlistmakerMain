package com.example.media.domain

import android.util.Log
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListInteractor
import com.example.media.domain.api.PlayListRepository
import com.example.player.ui.BottomAdapter
import com.example.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PlayListInteractorImpl(

    private val playListRepository: PlayListRepository
) : PlayListInteractor {

    override suspend fun addPlayList(playList: PlayList) {
        playListRepository.addPlayList(playList)
    }

    override fun getAllPlayList(): Flow<List<PlayList>> {
        return playListRepository.getAllPlayLists()
    }

    override suspend fun addTrackInPlayList(track: Track, playList: PlayList) : Boolean {
        val currentPlayList = playListRepository.getCurrentPlayList(playList.id).first()
        val type = object : TypeToken<List<Track>>() {}.type
        val trackList: MutableList<Track> = Gson().fromJson(currentPlayList.trackList, type) ?: mutableListOf()

        if(trackList.any { it.trackId == track.trackId }) {
            return false
        } else {
            trackList.add(track)
            val newTrackList = Gson().toJson(trackList)
            currentPlayList.trackList = newTrackList
            currentPlayList.trackCount = trackList.size
            playListRepository.updatePlayList(currentPlayList)
            return true
        }
    }
}