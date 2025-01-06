package com.example.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmakermain.R
import com.example.search.domain.model.Track

class TrackAdapter(private val trackList: MutableList<Track>, val trackListener: TrackListener): RecyclerView.Adapter<TrackViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view  = LayoutInflater.from(parent.context).inflate(R.layout.track_view,parent,false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size

    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            trackListener.onClick(trackList[position])
        }
    }

    interface TrackListener{
        fun onClick(track: Track)
    }

    fun updateData(newTrack: MutableList<Track>){
        trackList.clear()
        trackList.addAll(newTrack)
        notifyDataSetChanged()
    }

}