package com.example.search.domain.api

import com.example.search.domain.model.Track
import com.example.search.domain.consumer.Consumer
import kotlinx.coroutines.flow.Flow

interface TrackUseCaseInterface {
    fun execute(query: String) : Flow<Pair<MutableList<Track>?, String?>>
}