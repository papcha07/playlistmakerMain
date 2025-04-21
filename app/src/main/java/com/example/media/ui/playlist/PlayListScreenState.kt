package com.example.media.ui.playlist

import com.example.media.domain.api.PlayList

sealed interface PlayListScreenState {
    data object EmptyList : PlayListScreenState
    data class Content(val data: List<PlayList>) : PlayListScreenState
}
