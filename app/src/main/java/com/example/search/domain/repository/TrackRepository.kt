package com.example.search.domain.repository

import com.example.search.domain.model.Resource
import com.example.search.domain.model.Track

interface TrackRepository {
    fun searchTrack(query: String): Resource<MutableList<Track>>
}