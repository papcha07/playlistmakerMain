    package com.example.search.ui

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import com.example.creator.Creator
    import com.example.search.domain.consumer.Consumer
    import com.example.search.domain.consumer.ConsumerData
    import com.example.search.domain.model.Track

    class SearchViewModel: ViewModel() {
        private val searchInteractor = Creator.provideTracksUseCase()
        private val state = MutableLiveData<SearchActivityState>()
        fun getState() : LiveData<SearchActivityState>{
            return state
        }


        fun loadTrackList(query: String){
            state.value = SearchActivityState.Loading
            searchInteractor.execute(query, object : Consumer<MutableList<Track>> {
                override fun consume(data: ConsumerData<MutableList<Track>>) {
                    when(data){
                        is ConsumerData.Data ->{
                            state.postValue(SearchActivityState.Content(data.value))
                        }
                        is ConsumerData.Error ->{
                            if (data.message == "no internet" || data.message == "api-error"){
                                state.postValue(SearchActivityState.Error)
                            }
                            else{
                                state.postValue(SearchActivityState.NotFound)
                            }
                        }
                    }
                }
            })
        }


    }