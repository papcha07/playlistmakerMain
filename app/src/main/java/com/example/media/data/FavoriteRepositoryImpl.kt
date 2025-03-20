package com.example.media.data

import com.example.media.db.TrackDataBase
import com.example.media.db.TrackEntity
import com.example.media.domain.api.FavoriteRepository
import com.example.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val trackDbConverter: TrackDbConverter,
    private val db: TrackDataBase
) : FavoriteRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTrackListEntity = db.trackDao().getFavoriteTracks()
        emit(convertToTrackDto(favoriteTrackListEntity))
    }

    override fun getFavoriteTracksId(): Flow<List<Int>> = flow{
        val favoriteIndex = db.trackDao().getFavoriteTracksId()
        emit(favoriteIndex)
    }

    override suspend fun addTrack(track: Track) {
        val convertEntity = trackDbConverter.map(track)
        withContext(Dispatchers.IO) {
            db.trackDao().addTrack(convertEntity)
        }
    }

    override suspend fun deleteTrack(track: Track) {
        val convertEntity = trackDbConverter.map(track)
        withContext(Dispatchers.IO) {
            db.trackDao().deleteTrack(convertEntity)
        }
    }

    private fun convertToTrackDto(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track ->
            trackDbConverter.map(track)
        }
    }
}