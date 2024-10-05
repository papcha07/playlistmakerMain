package com.example.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ViewHolder.TrackViewHolder
import com.example.playlistmakermain.R
import com.example.playlistmakermain.Track

class TrackAdapter(val trackList: List<Track>): RecyclerView.Adapter<TrackViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view  = LayoutInflater.from(parent.context).inflate(R.layout.track_view,parent,false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size

    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(trackList[position])

    }


}