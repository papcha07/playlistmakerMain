package com.example.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.search.domain.model.Track
import com.example.playlistmakermain.R
import com.example.search.ui.SharedViewModel
import com.google.gson.Gson
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get


class PlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageButton
    private lateinit var timeTextView: TextView
    private var url = ""

    private val gson: Gson by inject()

    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        timeTextView = findViewById(R.id.currentTrackTimeId)
        playButton = findViewById(R.id.playButtonId)

        fillPlayer()

        playerViewModel = get { parametersOf(url) }

        playButton.setOnClickListener {
            togglePlayback()
        }

        playerViewModel.getState().observe(this){ state->
            when(state){
                is PlayerActivityState.Complete ->{
                    playButton.setImageResource(R.drawable.play)
                    timeTextView.setText("00:00")
                }
                else -> {}
            }
        }
        playerViewModel.getCurrentTimeState().observe(this) { currentTime ->
            timeTextView.text = currentTime

        }
        backToSearch()


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                playerViewModel.pause()
                finish()
            }
        })
    }
    override fun onStop() {
        super.onStop()
        playerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.release()
    }


    private fun backToSearch() {
        val backButton = findViewById<ImageButton>(R.id.backButtonMenu)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun fillPlayer(){
        val trackInfo = gson.fromJson(intent.getStringExtra("TRACK"), Track::class.java)
        val posterId = findViewById<ImageView>(R.id.posterId)
        val trackNameId = findViewById<TextView>(R.id.trackNameId)
        val group = findViewById<TextView>(R.id.groupId)
        val albomValueId = findViewById<TextView>(R.id.albomValueId)
        val yearValueId = findViewById<TextView>(R.id.yearValueId)
        val styleValueId = findViewById<TextView>(R.id.styleValueId)
        val countryValueId = findViewById<TextView>(R.id.countryValueId)
        val timeValueId = findViewById<TextView>(R.id.timeValueId)


        Glide.with(this).load(trackInfo.getCoverArtwork()).
        placeholder(R.drawable.placeholder).centerCrop().transform(
            RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.album_corner_radius))
        ).into(posterId)

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
        }
        else{
            playerViewModel.play()
            playButton.setImageResource(R.drawable.pause)
        }
    }

}

