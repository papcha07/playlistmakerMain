package com.example.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.PLAYLIST_MAKER_PREFERENCES
import com.example.player.data.MediaPlayerRepositoryImpl
import com.example.player.domain.repository.MediaPlayerRepository
import com.example.search.data.network.NetworkClient
import com.example.search.data.network.RetrofitClient
import com.example.search.data.network.RetrofitNetworkClient
import com.example.search.data.repository.TrackRepositoryImpl
import com.example.search.domain.repository.TrackRepository
import com.example.search.history.data.HistoryRepositoryImpl
import com.example.search.history.domain.repository.HistoryRepository
import com.example.settings.data.ThemeRepositoryImpl
import com.example.settings.domain.repository.ThemeRepository
import com.example.sharing.data.SharingRepository
import com.example.sharing.domain.repository.SharingRepositoryInterface
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

val dataModule = module {

    //история
    single<HistoryRepository> {
        HistoryRepositoryImpl(get(named("search_history_prefs")),get())
    }

    factory(named("search_history_prefs")){
        androidContext().getSharedPreferences("SEARCH_HISTORY",Context.MODE_PRIVATE)
    }

    single {
        Gson()
    }


    //поиск
    single<TrackRepository> {
         TrackRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single{
        RetrofitClient
    }

    //настройки
    factory<ThemeRepository>{
        ThemeRepositoryImpl(themeSharedPreferences = get(named("settings_prefs")))
    }

    factory(named("settings_prefs")){
        androidContext().getSharedPreferences(PLAYLIST_MAKER_PREFERENCES,Context.MODE_PRIVATE)
    }

    factory <SharingRepositoryInterface> {
        SharingRepository(context = androidContext())
    }

    //плеер

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single{
        MediaPlayer()
    }












}
