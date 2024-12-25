package com.example.domain.impl

import com.example.domain.consumer.Consumer
import com.example.domain.consumer.ConsumerData
import com.example.domain.consumer.ConsumerData.Error
import com.example.domain.model.Resource
import com.example.domain.model.Track
import com.example.domain.repository.TrackRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TracksUseCase(private val trackRepository: TrackRepository){
    private val executor = Executors.newSingleThreadExecutor()

    fun execute(query: String, consumer: Consumer<MutableList<Track>>){
        executor.execute{
            val tracksResource = trackRepository.searchTrack(query)

            when(tracksResource){
                is Resource.Failed ->{
                    consumer.consume(ConsumerData.Error(tracksResource.message))
                }
                is Resource.Success ->{
                    val tracks = tracksResource.data
                    consumer.consume(ConsumerData.Data(tracks))
                }
            }
        }
    }
}