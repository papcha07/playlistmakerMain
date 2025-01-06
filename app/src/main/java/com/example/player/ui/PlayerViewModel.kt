    package com.example.player.ui
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewmodel.initializer
    import androidx.lifecycle.viewmodel.viewModelFactory
    import com.example.creator.Creator

    class PlayerViewModel(url: String) : ViewModel() {
        private val playerInteractor = Creator.provideMediaPlayerInteractor(url)
        private val state = MutableLiveData<PlayerActivityState>()
        fun getState() : LiveData<PlayerActivityState> = state



        private val currentTimeState = MutableLiveData<String>()
        fun getCurrentTimeState(): LiveData<String>{
            return currentTimeState
        }

        init {
            completePlay()
        }

        fun play(){
            if(!playerInteractor.isPlaying()){
                state.postValue(PlayerActivityState.Play)
                playerInteractor.play()
            }
        }


        fun pause(){
            if(playerInteractor.isPlaying()){
                state.postValue(PlayerActivityState.Pause)
                playerInteractor.pause()
            }
        }

        fun updateCurrentTime() {
            currentTimeState.postValue(playerInteractor.getTrackTime())
        }

        fun release(){
            playerInteractor.release()
            state.postValue(PlayerActivityState.Release)
        }

        fun completePlay(){
            playerInteractor.setOnCompletionListener {
                state.postValue(PlayerActivityState.Complete)
            }
        }

        companion object{
            fun factory(url: String) = viewModelFactory {
                initializer {
                    PlayerViewModel(url)
                }
            }
        }


    }