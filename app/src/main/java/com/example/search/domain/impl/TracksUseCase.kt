package com.example.search.domain.impl

import com.example.search.domain.api.TrackUseCaseInterface
import com.example.search.domain.consumer.Consumer
import com.example.search.domain.consumer.ConsumerData
import com.example.search.domain.model.Resource
import com.example.search.domain.model.Track
import com.example.search.domain.repository.TrackRepository
import java.util.concurrent.Executors

class TracksUseCase(private val trackRepository: TrackRepository) : TrackUseCaseInterface {
    private val executor = Executors.newSingleThreadExecutor()

    override fun execute(query: String, consumer: Consumer<MutableList<Track>>){
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