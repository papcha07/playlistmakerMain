package com.example.search.data.network

import android.util.Log
import com.example.search.data.dto.Response
import com.example.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class RetrofitNetworkClient(private val networkService: RetrofitClient) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return try {
            if(dto is TrackSearchRequest){
                withContext(Dispatchers.IO){
                    val response = networkService.api.search(dto.query)
                    response.apply {
                        resultCode = 200
                    }
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
