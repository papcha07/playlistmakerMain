package com.example.domain.repository

interface MediaPlayerRepository {
    fun prepareTrack(url: String)
    fun play()
    fun pause()
    fun release()
    fun getTrackTime(): String
    fun isPlaying(): Boolean
    fun setOnCompletionListener(listener: () -> Unit)
}
