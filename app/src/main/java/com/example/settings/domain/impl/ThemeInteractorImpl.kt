package com.example.settings.domain.impl

import android.content.SharedPreferences
import com.example.settings.domain.repository.ThemeRepository
import com.example.settings.domain.api.ThemeInteractorInterface

class ThemeInteractorImpl(var themeRepository: ThemeRepository) : ThemeInteractorInterface {

    override fun getShared(): SharedPreferences{
        return themeRepository.getSharedPref()
    }

   override fun savedTheme(darkTheme: Boolean){
        themeRepository.savedTheme(darkTheme)
   }

    override fun getCurrentTheme(): Boolean{
        return themeRepository.getCurrentTheme()
    }

}