package com.example.search.history.ui

import com.example.media.domain.api.FavoriteInteractor
import com.example.search.domain.model.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class FavoriteTracksUpdater(private val favoriteInteractor: FavoriteInteractor) {

    suspend fun updateFavoriteStatus(tracks: List<Track>): List<Track> {
        val favoriteIds = favoriteInteractor.getFavoriteTracksId().firstOrNull() ?: emptySet()
        return tracks.map { track -> track.copy(isFavorite = track.trackId.toInt() in favoriteIds) }
    }


}