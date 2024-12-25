package com.example

import android.app.Application
import android.content.Context
import com.example.data.network.RetrofitNetworkClient
import com.example.data.repository.HistoryRepositoryImpl
import com.example.data.repository.MediaPlayerRepositoryImpl
import com.example.data.repository.ThemeRepositoryImpl
import com.example.data.repository.TrackRepositoryImpl
import com.example.domain.impl.HistoryInteractorImpl
import com.example.domain.impl.MediaPlayerInteractorImpl
import com.example.domain.impl.ThemeInteractorImpl
import com.example.domain.impl.TracksUseCase
import com.example.domain.repository.HistoryRepository
import com.example.domain.repository.MediaPlayerRepository
import com.example.domain.repository.ThemeRepository
import com.example.domain.repository.TrackRepository

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application){
        this.application = application
    }

    //тема приложения
    fun provideThemeInteractor() : ThemeInteractorImpl{
        return ThemeInteractorImpl(provideThemeRepository())
    }

    fun provideThemeRepository(): ThemeRepository{
        return ThemeRepositoryImpl(application)
    }

    //network
    fun provideTracksUseCase() : TracksUseCase{
        return TracksUseCase(provideTrackRepository())
    }

    fun provideTrackRepository(): TrackRepository{
        return TrackRepositoryImpl(provideRetrofitNetworkClient())
    }

    fun provideRetrofitNetworkClient(): RetrofitNetworkClient{
        return RetrofitNetworkClient()
    }

    //история
    fun provideHistoryInteractor(): HistoryInteractorImpl{
        return HistoryInteractorImpl(provideHistoryRepository())
    }
    fun provideHistoryRepository(): HistoryRepository{
        return HistoryRepositoryImpl(application)
    }

    //player
    fun provideMediaPlayerInteractor(url: String): MediaPlayerInteractorImpl{
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository(),url)
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository{
        return MediaPlayerRepositoryImpl()
    }


}