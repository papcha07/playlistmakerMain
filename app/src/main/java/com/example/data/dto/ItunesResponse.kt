package com.example.data.dto

import com.example.domain.model.Track

data class ItunesResponse(val resultCount: Int, val results: MutableList<Track>) : Response()
