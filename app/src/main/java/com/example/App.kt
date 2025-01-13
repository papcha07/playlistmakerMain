    package com.example

    import android.app.Application
    import android.content.res.Configuration
    import androidx.appcompat.app.AppCompatDelegate
    import com.example.di.appModule
    import com.example.di.dataModule
    import com.example.di.domainModule
    import com.example.di.viewModelModule
    import com.example.settings.domain.api.ThemeInteractorInterface
    import org.koin.android.ext.android.inject
    import org.koin.android.ext.koin.androidContext
    import org.koin.core.context.startKoin

    const val DARK_THEME_MODE = "dark_theme_key"
    const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"

    class App: Application(){
        var darkTheme: Boolean = false
        private val themeInteractor : ThemeInteractorInterface by inject()

        override fun onCreate() {
            super.onCreate()

            startKoin{
                androidContext(this@App)
                modules(listOf(appModule,dataModule, domainModule, viewModelModule))
            }

            val shared = themeInteractor.getShared()

            if(!shared.contains(DARK_THEME_MODE)){
                val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when(nightModeFlags){
                    Configuration.UI_MODE_NIGHT_YES ->{
                        savedTheme(true)
                        switchTheme(true)
                    }
                    Configuration.UI_MODE_NIGHT_NO ->{
                        savedTheme(false)
                        switchTheme(false)
                    }
                }
            }
            else{
                darkTheme = themeInteractor.getCurrentTheme()
                switchTheme(darkTheme)
            }
        }

        fun switchTheme(darkThemeEnabled: Boolean) {
            darkTheme = darkThemeEnabled
            AppCompatDelegate.setDefaultNightMode(
                if (darkThemeEnabled) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        fun savedTheme(darkTheme: Boolean){
            themeInteractor.savedTheme(darkTheme)
        }



    }