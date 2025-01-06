package com.example.creator

import android.app.Application
import com.example.search.data.network.RetrofitNetworkClient
import com.example.search.history.data.HistoryRepositoryImpl
import com.example.player.data.MediaPlayerRepositoryImpl
import com.example.settings.data.ThemeRepositoryImpl
import com.example.search.data.repository.TrackRepositoryImpl
import com.example.search.history.domain.api.HistoryInteractorInterface
import com.example.player.domain.api.MediaPlayerInteractorInterface
import com.example.search.history.domain.impl.HistoryInteractorImpl
import com.example.player.domain.impl.MediaPlayerInteractorImpl
import com.example.settings.domain.impl.ThemeInteractorImpl
import com.example.search.domain.impl.TracksUseCase
import com.example.search.history.domain.repository.HistoryRepository
import com.example.player.domain.repository.MediaPlayerRepository
import com.example.settings.domain.repository.ThemeRepository
import com.example.search.domain.repository.TrackRepository
import com.example.settings.domain.api.ThemeInteractorInterface
import com.example.search.domain.api.TrackUseCaseInterface
import com.example.sharing.data.SharingRepository
import com.example.sharing.domain.api.SharingInteractorInterface
import com.example.sharing.domain.interactor.SharingInteractorImpl
import com.example.sharing.domain.repository.SharingRepositoryInterface

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application){
        Creator.application = application
    }

    //тема приложения
    fun provideThemeInteractor() : ThemeInteractorInterface {
        return ThemeInteractorImpl(provideThemeRepository())
    }

    fun provideThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(application)
    }

    //network
    fun provideTracksUseCase() : TrackUseCaseInterface {
        return TracksUseCase(provideTrackRepository())
    }

    fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(provideRetrofitNetworkClient())
    }

    fun provideRetrofitNetworkClient(): RetrofitNetworkClient {
        return RetrofitNetworkClient()
    }

    //история
    fun provideHistoryInteractor(): HistoryInteractorInterface {
        return HistoryInteractorImpl(provideHistoryRepository())
    }
    fun provideHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl(application)
    }

    //player
    fun provideMediaPlayerInteractor(url: String): MediaPlayerInteractorInterface {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository(),url)
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    //sharing
    fun provideSharingInteractor() : SharingInteractorInterface{
        return SharingInteractorImpl(provideSharingRepository())
    }

    fun provideSharingRepository(): SharingRepositoryInterface{
        return SharingRepository(application)
    }


}