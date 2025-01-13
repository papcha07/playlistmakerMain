package com.example.settings.ui

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.settings.domain.api.ThemeInteractorInterface
import com.example.sharing.domain.api.SharingInteractorInterface
import com.example.sharing.domain.model.ShareDataInfo

class SettingsViewModel(
    private val themeInteractor: ThemeInteractorInterface,
    private val sharingInteractor: SharingInteractorInterface
    ) : ViewModel() {

    private val themeState = MutableLiveData<Boolean>()
    fun getState(): LiveData<Boolean>{
        return themeState
    }

    init {
        getState()
    }

    fun getCurrentTheme(): Boolean{
        return themeInteractor.getCurrentTheme()
    }

    fun openLink(link: String){
        sharingInteractor.openLink(link)
    }

    fun sendMessageToDeveloper(data: ShareDataInfo){
        sharingInteractor.messageSupport(data)
    }

    fun shareLink(link: String){
        sharingInteractor.shareLink(link)
    }


}