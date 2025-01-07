package com.example.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.search.domain.model.Track
import com.example.playlistmakermain.R
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerActivity : AppCompatActivity() {



    private lateinit var playButton: ImageButton
    private lateinit var timeTextView: TextView
    private var url = ""


    private val playerViewModel : PlayerViewModel by viewModel{
        parametersOf(url)
    }

    private val handler: Handler = Handler(Looper.getMainLooper())

    companion object{
        const val DELAY = 500
    }

    private val updateTrackTime = object : Runnable{
        override fun run() {
            playerViewModel.updateCurrentTime()
            handler.postDelayed(this, DELAY.toLong())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        timeTextView = findViewById(R.id.currentTrackTimeId)
        playButton = findViewById(R.id.playButtonId)
        fillPlayer()

        playButton.setOnClickListener {
            togglePlayback()
        }

        playerViewModel.getState().observe(this){ state->
            when(state){
                is PlayerActivityState.Complete ->{
                    playButton.setImageResource(R.drawable.play)
                    timeTextView.setText("00:30")
                }
                else -> {}
            }
        }
        playerViewModel.getCurrentTimeState().observe(this){
            currentTime ->
            timeTextView.setText(currentTime)
        }
        backToSearch()
    }
    override fun onStop() {
        super.onStop()
        playerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTrackTime)
        playerViewModel.release()
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
        if(playerViewModel.getState().value == PlayerActivityState.Play){
            playerViewModel.pause()
            playButton.setImageResource(R.drawable.play)
            handler.removeCallbacks(updateTrackTime)
        }
        else{
            playerViewModel.play()
            playButton.setImageResource(R.drawable.pause)
            handler.post(updateTrackTime)
        }
    }






}

