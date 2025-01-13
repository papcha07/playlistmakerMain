package com.example.search.history.data

import android.content.Context
import android.content.SharedPreferences
import com.example.search.domain.model.Track
import com.example.search.history.domain.repository.HistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : HistoryRepository {

    private val historyKey = "SEARCH_HISTORY"

    override fun addTrack(track: Track) {
        val history = getHistory().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > 10) {
            history.removeAt(10)
        }
        sharedPreferences.edit().putString(historyKey, gson.toJson(history)).apply()
    }

    override fun getHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(historyKey, null)
        return if (json == null) mutableListOf()
        else gson.fromJson(json, object : TypeToken<MutableList<Track>>() {}.type)
    }

    override fun clearHistory() {
        sharedPreferences.edit().clear().apply()
    }
}
