package com.example.player.ui

sealed interface PlayerActivityState {

    object Play: PlayerActivityState
    object Pause: PlayerActivityState
    object Complete: PlayerActivityState
    object Release: PlayerActivityState
}