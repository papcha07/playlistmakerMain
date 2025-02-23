package com.example.search.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val infoState = MutableLiveData<String>()

    fun getInfoState() : MutableLiveData<String>{
        return infoState
    }

    fun setInfo(trackInfo: String){
        infoState.value = trackInfo
    }

}