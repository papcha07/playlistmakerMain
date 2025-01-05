package com.example.presentation.playlistmakermain.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.Creator
import com.example.domain.api.MediaPlayerInteractorInterface
import com.example.domain.model.Track
import com.example.playlistmakermain.R
import com.google.gson.Gson


class PlayerActivity : AppCompatActivity() {



    private lateinit var playButton: ImageButton
    private lateinit var timeTextView: TextView
    private lateinit var playerUseCase: MediaPlayerInteractorInterface
    private var url = ""


    private val handler: Handler = Handler(Looper.getMainLooper())

    private val updateTrackTime = object : Runnable{
        override fun run() {
            timeTextView.text = playerUseCase.getTrackTime()
            handler.postDelayed(this, 500)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        timeTextView = findViewById(R.id.currentTrackTimeId)
        playButton = findViewById(R.id.playButtonId)
        fillPlayer()
        playerUseCase = Creator.provideMediaPlayerInteractor(url)

        playButton.setOnClickListener {
            togglePlayback()
        }

        playerUseCase.setOnCompletionListener {
            runOnUiThread {
                playButton.setImageResource(R.drawable.play)
                handler.removeCallbacks(updateTrackTime)
                timeTextView.setText("00:30")
            }
        }

        backToSearch()
    }
    override fun onStop() {
        super.onStop()
        playerUseCase.pause()
        handler.removeCallbacks(updateTrackTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerUseCase.release()
    }


    private fun backToSearch(){
        val backButton = findViewById<ImageButton>(R.id.backButtonMenu)
        backButton.setOnClickListener{
            finish()
        }
    }
    private fun fillPlayer(){
        val trackInfo = Gson().fromJson(intent.getStringExtra("TRACK"), Track::class.java)
        val posterId = findViewById<ImageView>(R.id.posterId)
        val trackNameId = findViewById<TextView>(R.id.trackNameId)
        val group = findViewById<TextView>(R.id.groupId)
        val albomValueId = findViewById<TextView>(R.id.albomValueId)
        val yearValueId = findViewById<TextView>(R.id.yearValueId)
        val styleValueId = findViewById<TextView>(R.id.styleValueId)
        val countryValueId = findViewById<TextView>(R.id.countryValueId)
        val timeValueId = findViewById<TextView>(R.id.timeValueId)


        Glide.with(this).load(trackInfo.getCoverArtwork()).
        placeholder(R.drawable.placeholder).centerCrop().transform(RoundedCorners(100)).into(posterId)

        url = trackInfo.previewUrl!!
        trackNameId.text = trackInfo.trackName
        group.text = trackInfo.artistName
        albomValueId.text = trackInfo.collectionName ?: ""
        timeValueId.text = trackInfo.trackTimeMillis
        yearValueId.text = trackInfo.releaseDate?.substring(0,4)
        styleValueId.text = trackInfo.primaryGenreName
        countryValueId.text = trackInfo.country
    }
    private fun togglePlayback(){
        if(playerUseCase.isPlaying()){
            playerUseCase.pause()
            playButton.setImageResource(R.drawable.play)
        }
        else{
            playerUseCase.play()
            playButton.setImageResource(R.drawable.pause)
            handler.post(updateTrackTime)

        }
    }






}

