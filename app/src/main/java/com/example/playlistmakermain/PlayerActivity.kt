package com.example.playlistmakermain

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYED = 2
        private const val STATE_PAUSED = 3
    }

    private lateinit var playButton: ImageButton
    private lateinit var timeTextView: TextView
    private var playerState = STATE_DEFAULT
    private var url = ""
    private val mediaPlayer = MediaPlayer()



    private val handler: Handler = Handler(Looper.getMainLooper())

    private val updateTrackTime = object : Runnable{
        override fun run() {
            if(playerState == STATE_PLAYED){
                val currentTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                timeTextView.text = currentTime
                handler.postDelayed(this,500)
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        backToSearch()
        fillPlayer()
        preparePlayer()

        timeTextView = findViewById(R.id.currentTrackTimeId)
        playButton = findViewById(R.id.playButtonId)
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onStop() {
        super.onStop()
        pausePlayer()
        playButton.setImageResource(R.drawable.play)
    }

    override fun onPause() {
        super.onPause()
        if (!isChangingConfigurations) {
            pausePlayer()
        }
    }


    private fun backToSearch(){
        val backButton = findViewById<ImageButton>(R.id.backButtonMenu)
        backButton.setOnClickListener{
            finish()
        }
    }
    private fun fillPlayer(){
        val trackInfo = Gson().fromJson(intent.getStringExtra("TRACK"),Track::class.java)
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


    private fun preparePlayer(){

        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener{
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            timeTextView.text = "00:00"
            playButton.setImageResource(R.drawable.play)

        }
    }


    private fun playbackControl(){
        when(playerState){
            STATE_PLAYED ->{
                playButton.setImageResource(R.drawable.play)
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED ->{
                playButton.setImageResource(R.drawable.pause)
                startPlayer()
            }
        }
    }

    private fun startPlayer(){
        handler.post(updateTrackTime)
        mediaPlayer.start()
        playerState = STATE_PLAYED
    }

    private fun pausePlayer(){
        handler.removeCallbacks(updateTrackTime)
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }



}

