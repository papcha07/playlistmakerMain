package com.example.search.history.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.domain.api.FavoriteInteractor
import com.example.search.domain.model.Track
import com.example.search.history.domain.api.HistoryInteractorInterface
import com.example.search.ui.SearchActivityState
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyInteractor: HistoryInteractorInterface,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {


    private val state = MutableLiveData<TrackActivityState>()
    fun getState(): LiveData<TrackActivityState> = state

    init {
        loadData()
    }

    private fun loadData() {
        val historyList = historyInteractor.getHistory()
        if(historyList.isNotEmpty()){
            viewModelScope.launch {
                val newTracks = favoriteInteractor.updateTrackStatus(historyList)
                state.postValue(TrackActivityState.Content(newTracks))
            }
        }
        else{
            TrackActivityState.Content(mutableListOf())
        }
    }

    fun updateTracksFavotiteStatus(){
        val trackState = state.value
        when(trackState){
            is TrackActivityState.Content -> {
                viewModelScope.launch {
                    val tracks = trackState.data
                    viewModelScope.launch {
                        val newTrackList = favoriteInteractor.updateTrackStatus(tracks)
                        state.postValue(TrackActivityState.Content(newTrackList))
                    }
                }
            }
            else -> {}
        }
    }


    fun addTrack(track: Track){
        historyInteractor.addTrack(track)
        val currentHistory = historyInteractor.getHistory()
        state.value = TrackActivityState.Content(currentHistory)
    }


    fun clearTrackList(){
        historyInteractor.clearHistory()
        val currentHistory = historyInteractor.getHistory()
        state.value = TrackActivityState.Content(currentHistory)
    }

    fun getCurrentCountTrack() = historyInteractor.getHistory().size





}