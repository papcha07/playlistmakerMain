package com.example.playlistmakermain

import android.app.Application
import android.content.SharedPreferences
import android.provider.SyncStateContract.Constants
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val DARK_THEME_MODE = "dark_theme_key"
class App: Application(){

    var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()
        val shared = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = shared.getBoolean(DARK_THEME_MODE,false)
        switchTheme(darkTheme)
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