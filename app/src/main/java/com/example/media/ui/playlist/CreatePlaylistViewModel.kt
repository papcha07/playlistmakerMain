package com.example.media.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListInteractor
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


}