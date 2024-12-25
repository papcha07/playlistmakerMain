package com.example.presentation.playlistmakermain.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmakermain.R
import com.example.domain.model.Track

class TrackViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
    private val trackGroup: TextView = itemView.findViewById(R.id.trackGroup)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
        }
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun bind(track: Track){

        if(track.trackName.length > 30) trackTitle.text = "${track.trackName.subSequence(0,25)}..."
        else trackTitle.text = track.trackName

        if(track.artistName.length > 20) trackGroup.text = "${track.artistName.subSequence(0,15)}..."
        else trackGroup.text = track.artistName

        trackTime.text = track.trackTimeMillis


        if(isNetworkAvailable(itemView.context)){
            Glide.with(itemView).
            load(track.artworkUrl100).
            placeholder(R.drawable.placeholder).
            centerCrop().transform(RoundedCorners(10)).
            into(trackImage)
        }
        else {
            trackImage.setImageResource(R.drawable.placeholder)
        }




    }

}