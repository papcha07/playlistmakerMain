package com.example.player.data

import android.media.MediaPlayer
import android.util.Log
import com.example.player.domain.repository.MediaPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var onCompletionListener: (() -> Unit)? = null


    override fun prepareTrack(url: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getTrackTime(): String {
        val currentTime = mediaPlayer.currentPosition
        Log.d("MediaPlayer", "Current position: $currentTime")
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
        mediaPlayer.setOnCompletionListener {
            onCompletionListener?.invoke()
        }
    }


}