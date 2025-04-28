package com.example.media.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.domain.api.FavoriteInteractor
import com.example.media.domain.api.PlayList
import com.example.media.domain.api.PlayListInteractor
import com.example.sharing.domain.api.SharingInteractorInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditPlayListViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel()
{

    val playlistData = MutableLiveData<PlayList>()
    val getPlaylistData: LiveData<PlayList> = playlistData


    fun loadData(id: Int) {
        viewModelScope.launch {
            val playList = playListInteractor.getPlayListById(id).first()
            playlistData.postValue(playList)
        }
    }

    fun updatePlayList(playList: PlayList){
        viewModelScope.launch {
            playListInteractor.updatePlayList(playList)
        }
    }


}