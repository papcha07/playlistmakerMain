package com.example.domain.impl

import android.content.SharedPreferences
import com.example.domain.repository.ThemeRepository

class ThemeInteractorImpl(var themeRepository: ThemeRepository) {

    fun getShared(): SharedPreferences{
        return themeRepository.getSharedPref()
    }

    fun savedTheme(darkTheme: Boolean){
        themeRepository.savedTheme(darkTheme)
    }

    fun getCurrentTheme(): Boolean{
        return themeRepository.getCurrentTheme()
    }


}