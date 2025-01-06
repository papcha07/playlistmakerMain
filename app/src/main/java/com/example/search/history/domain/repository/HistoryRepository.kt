package com.example.search.history.domain.repository

import com.example.search.domain.model.Track

interface HistoryRepository {
    fun addTrack(track: Track)
    fun getHistory(): MutableList<Track>
    fun clearHistory()
}