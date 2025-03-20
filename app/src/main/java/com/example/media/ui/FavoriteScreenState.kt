package com.example.media.ui

import com.example.search.domain.model.Track

sealed interface FavoriteScreenState {
    data class Content(val data: List<Track>) : FavoriteScreenState
    object Empty: FavoriteScreenState
}