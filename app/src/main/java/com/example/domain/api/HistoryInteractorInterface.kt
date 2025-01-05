package com.example.domain.api

import com.example.domain.model.Track

interface HistoryInteractorInterface {
    fun addTrack(track: Track)
    fun getHistory(): MutableList<Track>
    fun clearHistory()
}