package com.example.playlistmakermain

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_TRACKS_SHARED_PREF = "history_tracks_shared_pref"
const val HISTORY_TRACK = "history_tracks_key"
class SearchHistory(private val sharedPreferences: SharedPreferences) {



    private fun isContains(historyTrackList: MutableList<Track>, track: Track ): Boolean{
        historyTrackList.forEach{
            if(it.trackId == track.trackId){
                return true
            }
        }
        return false
    }

    fun addTrack(historyTrackList: MutableList<Track>, track: Track ){
        historyTrackList.removeAll{it.trackId == track.trackId}
        historyTrackList.add(0,track)

        if(historyTrackList.size > 10){
            historyTrackList.removeAt(historyTrackList.size - 1)
        }

        save(historyTrackList)
    }

    fun save(historyTrackList: MutableList<Track>){
        val json = Gson().toJson(historyTrackList)
        sharedPreferences.edit().putString(HISTORY_TRACK,json).apply()
    }

    fun getHistoryTrackList(jsonString: String): MutableList<Track>{
        val listType = object: TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(jsonString,listType)
    }

}