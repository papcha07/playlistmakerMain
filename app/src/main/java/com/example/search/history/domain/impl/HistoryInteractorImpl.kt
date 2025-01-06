package com.example.search.history.domain.impl

import com.example.search.domain.model.Track
import com.example.search.history.domain.repository.HistoryRepository
import com.example.search.history.domain.api.HistoryInteractorInterface

class HistoryInteractorImpl(private val historyRepository: HistoryRepository):
    HistoryInteractorInterface {

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