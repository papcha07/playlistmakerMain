package com.example.media.domain.api

import com.example.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor{
    fun getFavoriteTracks() : Flow<List<Track>>
    fun getFavoriteTracksId() : Flow<List<Int>>
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun updateTrackStatus(trackList: List<Track>) : List<Track>
}