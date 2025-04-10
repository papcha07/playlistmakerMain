package com.example.player.ui

import android.adservices.adid.AdId
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.media.domain.api.FavoriteInteractor
import com.example.player.domain.api.MediaPlayerInteractorInterface
import com.example.search.domain.model.Track
import com.example.search.history.ui.TrackActivityState
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: MediaPlayerInteractorInterface, url: String,
    private val favoriteInteractor: FavoriteInteractor
) :
    ViewModel() {

    private val state = MutableLiveData<PlayerActivityState>()
    fun getState(): LiveData<PlayerActivityState> = state

    private val currentTimeState = MutableLiveData<String>()
    fun getCurrentTimeState(): LiveData<String> {
        return currentTimeState
    }

    private val trackStatus = MutableLiveData<TrackFavoriteState>()
    fun getTrackStatus() : LiveData<TrackFavoriteState> {
        return trackStatus
    }

    init {
        completePlay()
    }

    fun addTrackToFavorite(track: Track){
        viewModelScope.launch {
            favoriteInteractor.addTrack(track)
            trackStatus.postValue(TrackFavoriteState.isFavorite)
        }
    }

    fun deleteTrackFromFavorite(track: Track){
        viewModelScope.launch {
            favoriteInteractor.deleteTrack(track)
            trackStatus.postValue(TrackFavoriteState.isNotFavorite)
        }
    }

    fun initStatus(track: Track){
        Log.d("INITSTATUS", "${track.isFavorite}")
        if(track.isFavorite){
            trackStatus.postValue(TrackFavoriteState.isFavorite)
        }
        else{
            trackStatus.postValue(TrackFavoriteState.isNotFavorite)
        }
    }

    fun play() {
        if (!playerInteractor.isPlaying()) {
            state.postValue(PlayerActivityState.Play)
            playerInteractor.play()
            updateCurrentTime()
        }
    }


    fun pause() {
        if (playerInteractor.isPlaying()) {
            state.postValue(PlayerActivityState.Pause)
            playerInteractor.pause()
        }
    }

    fun updateCurrentTime() {
        viewModelScope.launch {
            playerInteractor.getTrackTime().collect { time ->
                currentTimeState.postValue(time)

            }
        }
    }

    fun release() {
        playerInteractor.release()
        state.postValue(PlayerActivityState.Release)
    }

    fun completePlay() {
        playerInteractor.setOnCompletionListener {
            state.postValue(PlayerActivityState.Complete)
        }
    }

}