package com.example.media.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.domain.api.FavoriteInteractor
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListInteractor
import com.example.search.domain.model.Track
import com.example.sharing.domain.api.SharingInteractorInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val playListInteractor: PlayListInteractor,
    private val trackInteractor: FavoriteInteractor,
    private val sharingInteractorInterface: SharingInteractorInterface
) : ViewModel() {

    private val backState = MutableLiveData<Boolean>()
    fun getBackState(): LiveData<Boolean> {
        return backState
    }

    private val albumsState = MutableLiveData<PlayListScreenState>()
    fun getAlbumState(): LiveData<PlayListScreenState> {
        return albumsState
    }

    val mainScreenAlbumState = MutableLiveData<PlayList>()
    fun getMainScreenState () : LiveData<PlayList> = mainScreenAlbumState


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
            val newList = trackInteractor.updateTrackStatus(list)
            trackState.postValue(newList)
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
            val updatedPlayList = playListInteractor.getPlayListById(playList.id).first()
            mainScreenAlbumState.postValue(updatedPlayList)
        }
    }

    fun deletePlayList(id: Int) {
        viewModelScope.launch {
            playListInteractor.deletePlayListById(id)
        }
    }

    fun shareTrackList(playList: PlayList){
        sharingInteractorInterface.shareTrack(playList)
    }

    fun getPlayListById(id: Int){
        viewModelScope.launch {
            val playlist = playListInteractor.getPlayListById(id).first()
            mainScreenAlbumState.postValue(playlist)
        }
    }

}