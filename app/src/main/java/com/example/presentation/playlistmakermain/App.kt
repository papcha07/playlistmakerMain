package com.example.presentation.playlistmakermain

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.Creator
import com.example.domain.api.ThemeInteractorInterface

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val DARK_THEME_MODE = "dark_theme_key"

class App: Application(){
    var darkTheme: Boolean = false
    private lateinit var themeInteractor : ThemeInteractorInterface

    override fun onCreate() {
        Creator.initApplication(this)
        super.onCreate()
        themeInteractor = Creator.provideThemeInteractor()


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