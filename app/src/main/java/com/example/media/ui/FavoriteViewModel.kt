package com.example.media.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.TableInfo
import com.example.media.domain.api.FavoriteInteractor
import com.example.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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