package com.example.domain.api

import android.content.SharedPreferences

interface ThemeInteractorInterface {
    fun getShared() : SharedPreferences
    fun savedTheme(theme: Boolean)
    fun getCurrentTheme() : Boolean
}