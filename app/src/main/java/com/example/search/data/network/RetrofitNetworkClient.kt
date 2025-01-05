package com.example.search.data.network

import com.example.search.data.dto.Response
import com.example.search.data.dto.TrackSearchRequest

class RetrofitNetworkClient : NetworkClient {

    override fun doRequest(dto: Any): Response {
        return try {
            if(dto is TrackSearchRequest){
                val response = RetrofitClient.api.search(dto.query).execute()
                val networkResponse = response.body() ?: Response()
                networkResponse.apply {
                    resultCode = response.code()
                }
            }
            else{
                Response().apply {
                    resultCode = 400
                }
            }
        }
        catch (ex: Exception){
            Response().apply {
                resultCode = -1
            }

        }
    }
}
