package com.example.di

import com.example.media.ui.FavoriteViewModel
import com.example.media.ui.PlaylistViewModel
import com.example.player.ui.PlayerViewModel
import com.example.search.history.ui.HistoryViewModel
import com.example.search.ui.SearchViewModel
import com.example.search.ui.SharedViewModel
import com.example.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HistoryViewModel(get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        (url: String) ->
        PlayerViewModel(get{ parametersOf(url)}, url, get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        PlaylistViewModel()
    }


    viewModel{
        SharedViewModel()
    }


}