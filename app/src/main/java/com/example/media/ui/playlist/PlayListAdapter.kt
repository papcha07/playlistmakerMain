package com.example.media.ui.playlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.media.domain.api.PlayList
import com.example.playlistmakermain.R
import com.google.android.material.imageview.ShapeableImageView

class PlayListAdapter (
    private val albumList: MutableList<PlayList>,
    private val onItemClick: (PlayList) -> Unit

) : RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_view, parent, false)
        return PlayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(albumList[position])

        holder.itemView.setOnClickListener {
            onItemClick(albumList[position])
        }
    }

    fun setContent(list: List<PlayList>) {
        albumList.clear()
        albumList.addAll(list)
        notifyDataSetChanged()
    }
}

class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ShapeableImageView = itemView.findViewById(R.id.playlistImageId)
    private val playListName: TextView = itemView.findViewById(R.id.playListNameId)
    private val countView : TextView = itemView.findViewById(R.id.playListCountId)

    fun bind(playList: PlayList){
        val uri = playList.path
        when{
            uri == "" -> {
                imageView.setImageResource(R.drawable.placeholder)
            }
            else -> {
                imageView.setImageURI(playList.path?.toUri())
            }
        }
        playListName.text = playList.name
        countView.text = "${playList.trackCount} треков"
    }
}
