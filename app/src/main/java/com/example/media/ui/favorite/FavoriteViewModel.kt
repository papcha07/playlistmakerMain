package com.example.media.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.domain.api.FavoriteInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favInteractor: FavoriteInteractor) : ViewModel() {

    init {
        showTrackList()
    }

    private val favoriteTracks = MutableLiveData<FavoriteScreenState>()
    fun getFavoriteTracks(): LiveData<FavoriteScreenState> = favoriteTracks

     fun showTrackList() {
        viewModelScope.launch {
            val favTrackList = favInteractor.getFavoriteTracks().first()
            if (!favTrackList.isNullOrEmpty()) {
                val newTracks = favInteractor.updateTrackStatus(favTrackList)
                favoriteTracks.postValue(FavoriteScreenState.Content(newTracks))
            } else {
                favoriteTracks.postValue(FavoriteScreenState.Empty)
            }
        }
    }
}