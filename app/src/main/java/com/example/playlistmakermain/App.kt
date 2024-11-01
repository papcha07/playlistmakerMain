package com.example.playlistmakermain

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.provider.SyncStateContract.Constants
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val DARK_THEME_MODE = "dark_theme_key"

class App: Application(){
    var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val shared = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
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
            darkTheme = shared.getBoolean(DARK_THEME_MODE,false)
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
        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
            .edit()
            .putBoolean(DARK_THEME_MODE, darkTheme)
            .apply()
    }



}