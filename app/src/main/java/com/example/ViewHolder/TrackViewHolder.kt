package com.example.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmakermain.R
import com.example.playlistmakermain.Track

class TrackViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
    private val trackGroup: TextView = itemView.findViewById(R.id.trackGroup)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)


    fun bind(track: Track){

        if(track.trackName.length > 40) trackTitle.text = "${track.trackName.subSequence(0,30)}..."
        else trackTitle.text = track.trackName

        if(track.artistName.length > 20) trackGroup.text = "${track.artistName.subSequence(0,15)}..."
        else trackGroup.text = track.artistName

        trackTime.text = track.trackTime

        Glide.with(itemView).
        load(track.artworkUrl100).
        placeholder(R.drawable.placeholder).
        centerCrop().transform(RoundedCorners(10)).
        into(trackImage)

    }

}