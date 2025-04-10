package com.example.search.ui

import com.example.search.domain.model.Track

sealed interface SearchActivityState {
    data class Content(val data: List<Track>): SearchActivityState
    object Loading: SearchActivityState
    object NotFound: SearchActivityState
    object Error: SearchActivityState

}