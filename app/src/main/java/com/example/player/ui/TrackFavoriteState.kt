package com.example.player.ui

sealed interface TrackFavoriteState {
    object isFavorite : TrackFavoriteState
    object isNotFavorite: TrackFavoriteState
}
