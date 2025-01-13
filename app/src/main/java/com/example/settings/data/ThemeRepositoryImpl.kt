package com.example.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.example.settings.domain.repository.ThemeRepository
import com.example.PLAYLIST_MAKER_PREFERENCES

const val DARK_THEME_MODE = "dark_theme_key"

class ThemeRepositoryImpl(private val themeSharedPreferences: SharedPreferences) : ThemeRepository {

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