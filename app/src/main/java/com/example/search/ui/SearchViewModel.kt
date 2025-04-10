package com.example.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.domain.api.FavoriteInteractor
import com.example.search.domain.api.TrackUseCaseInterface
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackUseCaseInterface: TrackUseCaseInterface,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val searchInteractor = trackUseCaseInterface
    private val state = MutableLiveData<SearchActivityState>()
    fun getState(): LiveData<SearchActivityState> {
        return state
    }


    fun loadTrackList(query: String) {
        state.value = SearchActivityState.Loading
        viewModelScope.launch {
            searchInteractor.execute(query).collect { pair ->
                val data = pair.first
                val message = pair.second
                Log.d("track", "${data?.size}")
                Log.d("track", message.toString())
                if (data != null) {
                    viewModelScope.launch {
                        val tracks = data
                        viewModelScope.launch {
                            val newTrackList = favoriteInteractor.updateTrackStatus(tracks)
                            state.postValue(SearchActivityState.Content(newTrackList))
                        }
                    }
                } else {
                    if (message == "no internet" || message == "api-error") {
                        state.postValue(SearchActivityState.Error)
                    } else {
                        state.postValue(SearchActivityState.NotFound)
                    }
                }
            }
        }
    }

    fun updateTracksFavotiteStatus(){
        val trackState = state.value
        when(trackState){
            is SearchActivityState.Content -> {
                val tracks = trackState.data
                viewModelScope.launch {
                    val newTrackList = favoriteInteractor.updateTrackStatus(tracks)
                    state.postValue(SearchActivityState.Content(newTrackList))
                }
            }
            else -> {}
        }
    }
}