package com.example.domain.impl

import com.example.domain.api.MediaPlayerInteractorInterface
import com.example.domain.repository.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository, private val url: String):
    MediaPlayerInteractorInterface
{

    init {
        mediaPlayerRepository.prepareTrack(url)
    }

    override fun play(){
        mediaPlayerRepository.play()
    }

    override fun pause(){
        mediaPlayerRepository.pause()
    }

    override fun release(){
        mediaPlayerRepository.release()
    }

    override fun getTrackTime(): String{
        return mediaPlayerRepository.getTrackTime()
    }

    override fun isPlaying(): Boolean{
        return mediaPlayerRepository.isPlaying()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener(listener)
    }

}