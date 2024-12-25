package com.example.domain.impl

import com.example.domain.repository.MediaPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository, private val url: String) {

    init {
        mediaPlayerRepository.prepareTrack(url)
    }

    fun play(){
        mediaPlayerRepository.play()
    }

    fun pause(){
        mediaPlayerRepository.pause()
    }

    fun release(){
        mediaPlayerRepository.release()
    }

    fun getTrackTime(): String{
        return mediaPlayerRepository.getTrackTime()
    }

    fun isPlaying(): Boolean{
        return mediaPlayerRepository.isPlaying()
    }

    fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener(listener)
    }

}