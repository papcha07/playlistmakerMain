package com.example.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search.domain.api.TrackUseCaseInterface
import com.example.search.domain.consumer.Consumer
import com.example.search.domain.consumer.ConsumerData
import com.example.search.domain.model.Resource
import com.example.search.domain.model.Track
import kotlinx.coroutines.launch

class SearchViewModel(private val trackUseCaseInterface: TrackUseCaseInterface) : ViewModel() {
    private val searchInteractor = trackUseCaseInterface
    private val state = MutableLiveData<SearchActivityState>()
    fun getState(): LiveData<SearchActivityState> {
        return state
    }


    fun loadTrackList(query: String) {
        state.value = SearchActivityState.Loading
//            searchInteractor.execute(query, object : Consumer<MutableList<Track>> {
//                override fun consume(data: ConsumerData<MutableList<Track>>) {
//                    when(data){
//                        is ConsumerData.Data ->{
//                            state.postValue(SearchActivityState.Content(data.value))
//                        }
//                        is ConsumerData.Error ->{
//                            if (data.message == "no internet" || data.message == "api-error"){
//                                state.postValue(SearchActivityState.Error)
//                            }
//                            else{
//                                state.postValue(SearchActivityState.NotFound)
//                            }
//                        }
//                    }
//                }
//            })
        viewModelScope.launch {
            searchInteractor.execute(query).collect { pair ->
                val data = pair.first
                val message = pair.second
                Log.d("track", "${data?.size}")
                Log.d("track", message.toString())
                if (data != null) {
                    state.postValue(SearchActivityState.Content(data))
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
}