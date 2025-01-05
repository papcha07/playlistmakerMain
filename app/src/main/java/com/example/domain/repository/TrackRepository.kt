package com.example.domain.repository

import com.example.domain.model.Resource
import com.example.domain.model.Track

interface TrackRepository {
    fun searchTrack(query: String): Resource<MutableList<Track>>
}