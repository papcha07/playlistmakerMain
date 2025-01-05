package com.example.domain.api

import com.example.domain.consumer.Consumer
import com.example.domain.model.Track

interface TrackUseCaseInterface {


    fun execute(query: String, consumer: Consumer<MutableList<Track>>)
}