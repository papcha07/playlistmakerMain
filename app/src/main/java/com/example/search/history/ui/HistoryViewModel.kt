package com.example.search.history.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.search.domain.model.Track
import com.example.search.history.domain.api.HistoryInteractorInterface
import com.example.search.history.domain.repository.HistoryRepository

class HistoryViewModel(private val historyInteractor: HistoryInteractorInterface) : ViewModel() {


    private val state = MutableLiveData<TrackActivityState>()
    fun getState(): LiveData<TrackActivityState> = state

    init {
        loadData()
    }

    private fun loadData() {
        val historyList = historyInteractor.getHistory()
        state.value = if (historyList.isNotEmpty()) {
            TrackActivityState.Content(historyList)
        } else {
            TrackActivityState.Content(mutableListOf())
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