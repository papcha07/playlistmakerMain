package com.example.player.domain.api

import com.example.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaPlayerInteractorInterface {
    fun play()
    fun pause()
    fun release()
    fun getTrackTime(): Flow<String>
    fun isPlaying(): Boolean
    fun setOnCompletionListener(listener: () -> Unit)
}