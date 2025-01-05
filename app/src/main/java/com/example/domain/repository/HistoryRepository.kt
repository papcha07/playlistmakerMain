package com.example.domain.repository

import com.example.domain.model.Track

interface HistoryRepository {
    fun addTrack(track: Track)
    fun getHistory(): MutableList<Track>
    fun clearHistory()
}