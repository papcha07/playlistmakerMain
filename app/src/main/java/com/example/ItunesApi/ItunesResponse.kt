package com.example.ItunesApi

import com.example.playlistmakermain.Track

data class ItunesResponse(val resultCount: Int, val results: List<Track>)