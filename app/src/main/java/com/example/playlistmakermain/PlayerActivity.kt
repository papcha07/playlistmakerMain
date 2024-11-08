package com.example.playlistmakermain

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        backToSearch()
        fillPlayer()
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
        placeholder(R.drawable.placeholder).into(posterId)

        trackNameId.text = trackInfo.trackName
        group.text = trackInfo.artistName
        albomValueId.text = trackInfo.collectionName ?: ""
        timeValueId.text = trackInfo.trackTimeMillis
        yearValueId.text = trackInfo.releaseDate.substring(0,4)
        styleValueId.text = trackInfo.primaryGenreName
        countryValueId.text = trackInfo.country
    }



}

