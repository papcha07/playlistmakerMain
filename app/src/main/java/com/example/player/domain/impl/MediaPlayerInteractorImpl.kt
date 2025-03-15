package com.example.player.domain.impl

import com.example.player.domain.repository.MediaPlayerRepository
import com.example.player.domain.api.MediaPlayerInteractorInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override fun getTrackTime(): Flow<String> = flow {
        while(isPlaying()){
            emit(mediaPlayerRepository.getTrackTime())
            delay(300L)
        }
    }

    override fun isPlaying(): Boolean{
        return mediaPlayerRepository.isPlaying()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener(listener)
    }

}