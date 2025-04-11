package com.example.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePlaylistViewModel : ViewModel() {

    private val fieldsState = MutableLiveData<Boolean>()

    fun getFieldsState() : LiveData<Boolean>{
        return fieldsState
    }

    fun allFilled() {
        fieldsState.postValue(true)
    }

    fun notFilled(){
        fieldsState.postValue(false)
    }


}