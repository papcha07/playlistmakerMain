package com.example.search.data.dto

import com.example.search.domain.model.Track

data class ItunesResponse(val resultCount: Int, val results: MutableList<Track>) : Response()
