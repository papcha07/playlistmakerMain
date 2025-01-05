package com.example.domain.repository

import android.content.SharedPreferences

interface ThemeRepository {
    fun getSharedPref(): SharedPreferences
    fun savedTheme(darkTheme: Boolean)
    fun getCurrentTheme(): Boolean
}