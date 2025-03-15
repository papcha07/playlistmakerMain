package com.example.player.domain.repository

import kotlinx.coroutines.flow.Flow

interface MediaPlayerRepository {
    fun prepareTrack(url: String)
    fun play()
    fun pause()
    fun release()
    fun getTrackTime(): String
    fun isPlaying(): Boolean
    fun setOnCompletionListener(listener: () -> Unit)
}
