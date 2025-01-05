package com.example.domain.impl

import com.example.domain.api.HistoryInteractorInterface
import com.example.domain.model.Track
import com.example.domain.repository.HistoryRepository

class HistoryInteractorImpl(private val historyRepository: HistoryRepository): HistoryInteractorInterface  {

    override fun addTrack(track: Track) {
        historyRepository.addTrack(track)
    }

    override fun getHistory(): MutableList<Track> {
        return historyRepository.getHistory()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

}