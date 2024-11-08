package com.example.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.ViewHolder.TrackViewHolder
import com.example.playlistmakermain.R
import com.example.playlistmakermain.Track
import com.google.gson.Gson

class TrackAdapter(val trackList: List<Track>, val trackListener: TrackListener): RecyclerView.Adapter<TrackViewHolder>(){



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


}