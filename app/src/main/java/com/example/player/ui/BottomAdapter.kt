package com.example.player.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.media.domain.api.PlayList
import com.example.media.ui.playlist.PlayListViewHolder
import com.example.playlistmakermain.R
import com.google.android.material.imageview.ShapeableImageView

class BottomAdapter(private val albumList: MutableList<PlayList>) : RecyclerView.Adapter<BottomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_playlist_item, parent, false)
        return BottomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    fun setContent(list: List<PlayList>) {
        albumList.clear()
        albumList.addAll(list)
        notifyDataSetChanged()
    }
}

class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ShapeableImageView = itemView.findViewById(R.id.plImageId)
    private val playListName: TextView = itemView.findViewById(R.id.playlistNameId)
    private val countView : TextView = itemView.findViewById(R.id.playlistCountId)

    fun bind(playList: PlayList){
        imageView.setImageURI(playList.path.toUri())
        playListName.text = playList.name
        countView.text = "${playList.trackCount} треков"
    }
}
