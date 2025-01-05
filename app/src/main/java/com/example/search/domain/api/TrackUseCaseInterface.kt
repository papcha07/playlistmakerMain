package com.example.search.domain.api

import com.example.search.domain.model.Track
import com.example.search.domain.consumer.Consumer

interface TrackUseCaseInterface {


    fun execute(query: String, consumer: Consumer<MutableList<Track>>)
}