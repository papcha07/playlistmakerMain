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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.media.ui.FavoriteViewModel
import com.example.search.domain.model.Track
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.ActivityMediaPlayerBinding
import com.example.search.history.ui.TrackActivityState
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
    private lateinit var binding: ActivityMediaPlayerBinding
    private lateinit var track : Track
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = getTrack()

        timeTextView = findViewById(R.id.currentTrackTimeId)
        playButton = findViewById(R.id.playButtonId)
        fillPlayer(track)

        playButton.setOnClickListener {
            togglePlayback()
        }

        playerViewModel.getState().observe(this) { state ->
            when (state) {
                is PlayerActivityState.Complete -> {
                    binding.playButtonId.setImageResource(R.drawable.play)
                    binding.currentTrackTimeId.setText("00:00")
                }

                else -> {}
            }
        }


        playerViewModel.getCurrentTimeState().observe(this) { currentTime ->
            binding.currentTrackTimeId.setText(currentTime)
        }


        toggleLikeButton(track)

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


    private fun backToSearch() {

        binding.backButtonMenu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun fillPlayer(track: Track) {
        val trackInfo = track
        val posterId = findViewById<ImageView>(R.id.posterId)

        Glide.with(this).load(trackInfo.getCoverArtwork()).placeholder(R.drawable.placeholder)
            .centerCrop().transform(
                RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.album_corner_radius))
            ).into(posterId)

        url = trackInfo.previewUrl!!
        playerViewModel = get { parametersOf(url) }


        binding.trackNameId.text = trackInfo.trackName
        binding.groupId.text = trackInfo.artistName
        binding.albomValueId.text = trackInfo.collectionName ?: ""
        binding.timeValueId.text = trackInfo.trackTimeMillis
        binding.yearValueId.text = trackInfo.releaseDate?.substring(0, 4)
        binding.styleValueId.text = trackInfo.primaryGenreName
        binding.countryValueId.text = trackInfo.country
        playerViewModel.initStatus(trackInfo)
        Log.d("TRACKSTATUS", "${trackInfo.isFavorite}")

        playerViewModel.getTrackStatus().observe(this){
            status ->
            when(status){
                is TrackFavoriteState.isFavorite -> {
                    binding.likeButtonId.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.isfav))
                }
                is TrackFavoriteState.isNotFavorite -> {
                    binding.likeButtonId.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like))
                }
            }
        }
    }

    private fun togglePlayback() {
        if (playerViewModel.getState().value == PlayerActivityState.Play) {
            playerViewModel.pause()
            binding.playButtonId.setImageResource(R.drawable.play)
        } else {
            playerViewModel.play()
            binding.playButtonId.setImageResource(R.drawable.pause)
        }
    }

    private fun toggleLikeButton(track: Track){
        binding.likeButtonId.setOnClickListener {
            when {
                track.isFavorite -> {
                    deleteTrack(track)
                    track.isFavorite = false
                    binding.likeButtonId.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like))
                }
                !track.isFavorite -> {
                    addTrack(track)
                    track.isFavorite = true
                    binding.likeButtonId.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.isfav))
                }
            }
        }
    }

    private fun deleteTrack(track: Track){
        playerViewModel.deleteTrackFromFavorite(track)
    }

    private fun addTrack(track: Track){
        playerViewModel.addTrackToFavorite(track)
    }

    private fun getTrack() : Track{
        return gson.fromJson(intent.getStringExtra("TRACK"), Track::class.java)
    }

}