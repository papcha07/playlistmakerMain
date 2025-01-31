package com.example.di

import com.example.media.FavoriteViewModel
import com.example.media.PlaylistViewModel
import com.example.player.domain.api.MediaPlayerInteractorInterface
import com.example.player.ui.PlayerViewModel
import com.example.search.history.ui.HistoryViewModel
import com.example.search.ui.SearchViewModel
import com.example.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HistoryViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        (url: String) ->
        val interactor: MediaPlayerInteractorInterface = get{ parametersOf(url)}
        PlayerViewModel(get{ parametersOf(url)}, url)
    }

    viewModel {
        FavoriteViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }


}