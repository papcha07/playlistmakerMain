package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.model.Track
import com.example.domain.repository.HistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryRepositoryImpl(context: Context) : HistoryRepository{
    private var historyKey = "SEARCH_HISTORY"
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(historyKey, Context.MODE_PRIVATE)
    override fun addTrack(track: Track) {

        val history = getHistory().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > 10) {
            history.removeAt(10)
        }
        val json = Gson().toJson(history)
        sharedPreferences.edit().putString(historyKey,json).apply()
    }

    override fun getHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(historyKey,null)
        if(json == null) return mutableListOf()
        else {

            val type = object : TypeToken<MutableList<Track>>() {}.type
            return Gson().fromJson(json,type)
        }
    }

    override fun clearHistory() {
        sharedPreferences.edit().clear().apply()
    }

}