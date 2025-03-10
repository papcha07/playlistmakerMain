package com.example.search.domain.impl

import com.example.search.domain.api.TrackUseCaseInterface
import com.example.search.domain.consumer.Consumer
import com.example.search.domain.consumer.ConsumerData
import com.example.search.domain.model.Resource
import com.example.search.domain.model.Track
import com.example.search.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

class TracksUseCase(private val trackRepository: TrackRepository) : TrackUseCaseInterface {
    private val executor = Executors.newSingleThreadExecutor()

//    override fun execute(query: String, consumer: Consumer<MutableList<Track>>) {
//        executor.execute {
//            val tracksResource = trackRepository.searchTrack(query)
//
//            when (tracksResource) {
//                is Resource.Failed -> {
//                    consumer.consume(ConsumerData.Error(tracksResource.message))
//                }
//
//                is Resource.Success -> {
//                    val tracks = tracksResource.data
//                    val formattedTracks = tracks.map { track ->
//                        val trackTimeMillisLong = track.trackTimeMillis.toLongOrNull() ?: 0L
//                        val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
//                        val newTime = sdf.format(Date(trackTimeMillisLong))
//                        Track(
//                            track.trackId,
//                            track.trackName,
//                            track.artistName,
//                            newTime,
//                            track.artworkUrl100,
//                            track.collectionName,
//                            track.releaseDate,
//                            track.primaryGenreName,
//                            track.country,
//                            track.previewUrl
//                        )
//                    }.toMutableList()
//
//                    consumer.consume(ConsumerData.Data(formattedTracks))
//                }
//
//            }
//        }
//    }

    override fun execute(query: String): Flow<Pair<MutableList<Track>?, String?>> {
        return trackRepository.searchTrack(query).map {
            result ->
            when(result){
                is Resource.Failed -> Pair(null, result.message)

                is Resource.Success -> {

                    val tracks = result.data
                    val formattedTracks = tracks.map { track ->
                        val trackTimeMillisLong = track.trackTimeMillis.toLongOrNull() ?: 0L
                        val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
                        val newTime = sdf.format(Date(trackTimeMillisLong))
                        Track(
                            track.trackId,
                            track.trackName,
                            track.artistName,
                            newTime,
                            track.artworkUrl100,
                            track.collectionName,
                            track.releaseDate,
                            track.primaryGenreName,
                            track.country,
                            track.previewUrl
                        )
                    }.toMutableList()
                    Pair(formattedTracks, null)
                }
            }
        }
    }
}