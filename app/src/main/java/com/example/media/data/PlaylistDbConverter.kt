package com.example.media.data

import com.example.media.db.PlaylistEntity
import com.example.media.domain.api.PlayList

class PlaylistDbConverter {
    fun map(playlist: PlayList) : PlaylistEntity{
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.path,
            playlist.trackCount
        )
    }

    fun map(playlist: PlaylistEntity) : PlayList{
        return PlayList(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.path,
            playlist.trackCount
        )
    }
}
