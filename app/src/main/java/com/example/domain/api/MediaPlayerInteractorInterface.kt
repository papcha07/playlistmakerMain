package com.example.domain.api

interface MediaPlayerInteractorInterface {
    fun play()
    fun pause()
    fun release()
    fun getTrackTime(): String
    fun isPlaying(): Boolean
    fun setOnCompletionListener(listener: () -> Unit)
}