package com.example.media.data

import com.example.media.db.TrackEntity
import com.example.search.domain.model.Track

class TrackDbConverter {

    fun map(track: Track) : TrackEntity{
        return TrackEntity(
            track.trackId.toInt(),
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackEntity) : Track{
        return Track(
            track.id.toString(),
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}