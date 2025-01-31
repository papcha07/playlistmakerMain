    package com.example.di

    import com.example.player.domain.api.MediaPlayerInteractorInterface
    import com.example.player.domain.impl.MediaPlayerInteractorImpl
    import com.example.player.ui.PlayerViewModel
    import com.example.search.domain.api.TrackUseCaseInterface
    import com.example.search.domain.impl.TracksUseCase
    import com.example.search.history.domain.api.HistoryInteractorInterface
    import com.example.search.history.domain.impl.HistoryInteractorImpl
    import com.example.settings.domain.api.ThemeInteractorInterface
    import com.example.settings.domain.impl.ThemeInteractorImpl
    import com.example.sharing.domain.api.SharingInteractorInterface
    import com.example.sharing.domain.interactor.SharingInteractorImpl
    import org.koin.core.parameter.parametersOf
    import org.koin.dsl.factory
    import org.koin.dsl.module

    val domainModule = module {

        factory<HistoryInteractorInterface> {
            HistoryInteractorImpl(get())
        }

        factory<TrackUseCaseInterface>{
            TracksUseCase(get())
        }

        factory<ThemeInteractorInterface>{
            ThemeInteractorImpl(get())
        }

        factory<SharingInteractorInterface>{
            SharingInteractorImpl(get())
        }

        factory<MediaPlayerInteractorInterface>{
            (url: String) ->
            MediaPlayerInteractorImpl(get(), url)
        }

    }