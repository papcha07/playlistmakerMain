package com.example.search.data.network

import com.example.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}