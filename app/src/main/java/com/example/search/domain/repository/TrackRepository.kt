package com.example.search.domain.repository

import com.example.search.domain.model.Resource
import com.example.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(query: String): Flow<Resource<MutableList<Track>>>
}