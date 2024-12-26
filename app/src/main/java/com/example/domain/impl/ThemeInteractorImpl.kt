package com.example.domain.impl

import android.content.SharedPreferences
import com.example.domain.repository.ThemeRepository
import com.example.domain.api.ThemeInteractorInterface

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