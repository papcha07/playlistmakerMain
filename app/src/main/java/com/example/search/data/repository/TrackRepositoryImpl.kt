package com.example.search.data.repository

import com.example.search.data.dto.ItunesResponse
import com.example.search.data.dto.TrackSearchRequest
import com.example.search.data.network.NetworkClient
import com.example.search.domain.model.Resource
import com.example.search.domain.model.Track
import com.example.search.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {


    override fun searchTrack(query: String): Flow<Resource<MutableList<Track>>> = flow {

        val codeResponse = networkClient.doRequest(TrackSearchRequest(query))
        when (codeResponse.resultCode) {
            200 -> {
                val tracks = (codeResponse as ItunesResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }.toMutableList()

                if (tracks.isNullOrEmpty()) {

                    emit(Resource.Failed("not found"))
                } else {
                    emit(Resource.Success(tracks))
                }
            }

            -1 -> emit(Resource.Failed("no internet"))

            else -> emit(Resource.Failed("api error"))

        }
    }

}