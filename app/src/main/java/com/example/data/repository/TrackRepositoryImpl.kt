package com.example.data.repository

import android.util.Log
import com.example.data.dto.ItunesResponse
import com.example.data.network.NetworkClient
import com.example.data.dto.TrackSearchRequest
import com.example.domain.model.Resource
import com.example.domain.repository.TrackRepository
import com.example.domain.model.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {


    override fun searchTrack(query: String): Resource<MutableList<Track>> {
        val codeResponse = networkClient.doRequest(TrackSearchRequest(query))


        when(codeResponse.resultCode){
            200 ->{
                val tracks = (codeResponse as ItunesResponse).results.map {Track(
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