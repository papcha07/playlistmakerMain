package com.example.search.data.repository

import com.example.search.data.dto.ItunesResponse
import com.example.search.data.dto.TrackSearchRequest
import com.example.search.data.network.NetworkClient
import com.example.search.domain.model.Resource
import com.example.search.domain.model.Track
import com.example.search.domain.repository.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {


    override fun searchTrack(query: String): Resource<MutableList<Track>> {
        val codeResponse = networkClient.doRequest(TrackSearchRequest(query))
        when(codeResponse.resultCode){
            200 ->{
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
                    it.previewUrl)
                }.toMutableList()

                if(tracks.isNullOrEmpty()){
                    return Resource.Failed("not found")
                }
                else{
                    return Resource.Success(tracks)
                }
            }
            -1 -> return Resource.Failed("no internet")

            else -> return Resource.Failed("api error")

        }

    }

}