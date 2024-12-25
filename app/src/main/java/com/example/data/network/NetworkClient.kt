package com.example.data.network

import com.example.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}