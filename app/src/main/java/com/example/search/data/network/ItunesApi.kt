package com.example.search.data.network

import com.example.search.data.dto.ItunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): ItunesResponse
}