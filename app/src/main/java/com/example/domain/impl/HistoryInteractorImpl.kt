package com.example.domain.impl

import com.example.domain.model.Track
import com.example.domain.repository.HistoryRepository

class HistoryInteractorImpl(private val historyRepository: HistoryRepository)  {

    fun addTrack(track: Track) {
        historyRepository.addTrack(track)
    }

    fun getHistory(): MutableList<Track> {
        return historyRepository.getHistory()
    }

    fun clearHistory() {
        historyRepository.clearHistory()
    }

}