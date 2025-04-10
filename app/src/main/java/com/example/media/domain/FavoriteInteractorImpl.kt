package com.example.media.domain

import com.example.media.domain.api.FavoriteInteractor
import com.example.media.domain.api.FavoriteRepository
import com.example.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override fun getFavoriteTracksId(): Flow<List<Int>> {
        return favoriteRepository.getFavoriteTracksId()
    }

    override suspend fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }

    override suspend fun updateTrackStatus(trackList: List<Track>): List<Track> {
        val favoriteIds = getFavoriteTracksId().first()
        return trackList.onEach { it.isFavorite = it.trackId.toInt() in favoriteIds }
    }


}