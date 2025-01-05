package com.example.search.history.ui

import com.example.search.domain.model.Track

sealed interface TrackActivityState {
    data class Content(val data: MutableList<Track>): TrackActivityState
}