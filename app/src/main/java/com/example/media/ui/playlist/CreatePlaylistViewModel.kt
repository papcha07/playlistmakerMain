package com.example.media.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListInteractor
import com.example.search.domain.model.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playListInteractor: PlayListInteractor) : ViewModel() {

    private val backState = MutableLiveData<Boolean>()
    fun getBackState(): LiveData<Boolean> {
        return backState
    }

    private val albumsState = MutableLiveData<PlayListScreenState>()
    fun getAlbumState(): LiveData<PlayListScreenState> {
        return albumsState
    }


    private val addedState = MutableLiveData<Boolean?>()
    fun getAddedState(): LiveData<Boolean?> = addedState


    private val trackState = MutableLiveData<List<Track>>()
    fun getTrackState () : LiveData<List<Track>> = trackState

    init {
        backState.value = true
        getAllPlayLists()
    }

    fun canOut() {
        backState.postValue(true)
    }

    fun cantOut() {
        backState.postValue(false)
    }

    fun savePlayList(playList: PlayList) {
        viewModelScope.launch {
            playListInteractor.addPlayList(playList)
        }
    }

    fun getAllPlayLists() {
        viewModelScope.launch {
            val list = playListInteractor.getAllPlayList().first()
            when {
                list == null -> albumsState.postValue(PlayListScreenState.EmptyList)
                else -> {
                    albumsState.postValue(PlayListScreenState.Content(list))
                }
            }
        }
    }


    fun getTracksByPlayListId(id: Int) {
        viewModelScope.launch {
            val list = playListInteractor.getTracksById(id).first()
            trackState.postValue(list)
        }
    }

    fun addTrackInPlayList(track: Track, playList: PlayList) {
        viewModelScope.launch {
            val added = playListInteractor.addTrackInPlayList(track, playList)
            addedState.postValue(added)
            getAllPlayLists()
        }
    }

    fun clearAddedState() {
        addedState.postValue(null)
    }

    fun deleteTrack(track: Track, playList: PlayList) {
        viewModelScope.launch {
            playListInteractor.deleteTrack(track, playList)
            getTracksByPlayListId(playList.id)
        }
    }

}