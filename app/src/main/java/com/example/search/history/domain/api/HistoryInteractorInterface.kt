package com.example.search.history.domain.api

import com.example.search.domain.model.Track

interface HistoryInteractorInterface {
    fun addTrack(track: Track)
    fun getHistory(): MutableList<Track>
    fun clearHistory()
}