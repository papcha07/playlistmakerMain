package com.example.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.player.domain.api.MediaPlayerInteractorInterface
import kotlinx.coroutines.launch

class PlayerViewModel(private val playerInteractor: MediaPlayerInteractorInterface, url: String) :
    ViewModel() {

    private val state = MutableLiveData<PlayerActivityState>()
    fun getState(): LiveData<PlayerActivityState> = state


    private val currentTimeState = MutableLiveData<String>()
    fun getCurrentTimeState(): LiveData<String> {
        return currentTimeState
    }

    init {
        completePlay()
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
            playerInteractor.getTrackTime().collect{
                time ->
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