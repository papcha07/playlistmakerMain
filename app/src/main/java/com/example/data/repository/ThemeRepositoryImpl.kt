package com.example.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.domain.repository.ThemeRepository
import com.example.presentation.playlistmakermain.PLAYLIST_MAKER_PREFERENCES

const val PLAY_LIST_MAKER_THEME_PREFERENCES = "playlist_maker_theme_preferences"
const val DARK_THEME_MODE = "dark_theme_key"

class ThemeRepositoryImpl(context: Context) : ThemeRepository {

    private val themeSharedPreferences: SharedPreferences = context.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)

    override fun getSharedPref(): SharedPreferences {
        return themeSharedPreferences
    }

    override fun savedTheme(darkTheme: Boolean) {
        themeSharedPreferences.edit().putBoolean(DARK_THEME_MODE, darkTheme).apply()
    }

    override fun getCurrentTheme(): Boolean {
        return themeSharedPreferences.getBoolean(DARK_THEME_MODE, false)
    }


}