package com.example.playlistmakermain

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapter.TrackAdapter
import com.example.ItunesApi.ItunesApi
import com.example.ItunesApi.ItunesResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Queue
import kotlin.math.log


class SearchActivity : AppCompatActivity(), TrackAdapter.TrackListener {



    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private var isClickAllowed = true
    private val handler: Handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean{
        val current = isClickAllowed
        if(isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    private val searchRunnable = Runnable {
        performSearch(editText.text.toString())
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }




    private lateinit var editText: EditText
    private lateinit var closeButton: ImageView
    private lateinit var recyclerViewId: RecyclerView



    private lateinit var progressBar: ProgressBar
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var clearHistoryButton : Button
    private lateinit var youSearchId: TextView


    private lateinit var notFoundFrameLayout: FrameLayout
    private lateinit var errorInternetFrameLayout: FrameLayout
    private lateinit var refreshButton : Button




    private var editTextValue: String = ""

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private val itunesServiceApi = retrofit.create(ItunesApi::class.java)



    //track adapter
    private val trackList: MutableList<Track> = mutableListOf()
    private var trackAdapter = TrackAdapter(trackList,this)

    //history adapter
    private var historyTrackList: MutableList<Track> = mutableListOf()
    private var historyTrackAdapter = TrackAdapter(historyTrackList,this)

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        sharedPreferences = getSharedPreferences(HISTORY_TRACKS_SHARED_PREF, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        progressBar = findViewById(R.id.progressBarId)

        val savedHistoryJson = sharedPreferences.getString(HISTORY_TRACK,null)
        if(savedHistoryJson != null){
            historyTrackList = searchHistory.getHistoryTrackList(savedHistoryJson.toString())
        }



        //адаптер
        recyclerViewId = findViewById(R.id.trackList)
        trackAdapter = TrackAdapter(trackList,this)
        historyTrackAdapter = TrackAdapter(historyTrackList,this)
        recyclerViewId.adapter = trackAdapter

        historyRecyclerView = findViewById(R.id.historyRecyclerViewId)
        clearHistoryButton = findViewById(R.id.clearHistoryButtonId)
        historyRecyclerView.adapter = historyTrackAdapter
        youSearchId = findViewById(R.id.youSearhId)


        notFoundFrameLayout = findViewById(R.id.notFoundId)
        errorInternetFrameLayout = findViewById(R.id.errorInternetId)
        refreshButton = findViewById(R.id.refreshButtonId)

        historyTrackAdapter
        //элементы editText
        editText = findViewById(R.id.searchId)
        closeButton = findViewById(R.id.clearButtonId)



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.hasFocus() && (editText.text.isNullOrEmpty() || editText.text.toString() == "") && historyTrackList.isNotEmpty()) {
                    recyclerViewId.visibility = View.GONE
                    errorInternetFrameLayout.visibility = View.GONE
                    notFoundFrameLayout.visibility = View.GONE
                    youSearchId.visibility = View.VISIBLE
                    historyRecyclerView.visibility = View.VISIBLE
                    clearHistoryButton.visibility = View.VISIBLE
                }
                else {
                    closeButton.visibility = View.VISIBLE
                    historyRecyclerView.visibility = View.GONE
                    clearHistoryButton.visibility = View.GONE
                    youSearchId.visibility = View.GONE
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearTrackList(trackAdapter)
                }
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && (editText.text.isNullOrEmpty() || editText.text.toString() == "") && historyTrackList.isNotEmpty()) {
                recyclerViewId.visibility = View.GONE
                errorInternetFrameLayout.visibility = View.GONE
                notFoundFrameLayout.visibility = View.GONE
                historyRecyclerView.visibility = View.VISIBLE
                clearHistoryButton.visibility = View.VISIBLE
                youSearchId.visibility = View.VISIBLE
            }
            else{
                historyRecyclerView.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                youSearchId.visibility = View.GONE
            }
        }


        editText.addTextChangedListener(textWatcher)

        clearHistoryButton.setOnClickListener{
            clearHistoryTrackList()
        }


        refreshButton.setOnClickListener {
            searchDebounce()
        }

        editText.setOnClickListener {
            showKeyboard(editText)
        }

        closeButton.setOnClickListener {
            editText.setText("")
            closeKeyboard(editText)
            clearTrackList(trackAdapter)
            closeButton.visibility = View.GONE

        }


        val backButton = findViewById<ImageView>(R.id.searchBackButton)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }



    }

    private fun clearHistoryTrackList(){
        clearHistoryButton.visibility = View.GONE
        youSearchId.visibility = View.GONE
        historyRecyclerView.visibility = View.GONE
        historyTrackList.clear()
        sharedPreferences.edit().remove(HISTORY_TRACK).apply()
        historyTrackAdapter.notifyDataSetChanged()
    }

    private fun toMinuteMillisec(millisecond: String): String{
        val date = Date(millisecond.toLong())
        val format = SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)
        return format
    }
    private fun clearTrackList(adapter: TrackAdapter){
        trackList.clear()
        adapter.notifyDataSetChanged()
        recyclerViewId.visibility = View.VISIBLE

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_TEXT", editTextValue)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextValue = savedInstanceState.getString("SEARCH_TEXT", "")
        editText.setText(editTextValue)
    }
    private fun showKeyboard(view: View) {
        view.requestFocus()
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun closeKeyboard(view: View) {
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(view.windowToken, 0)
    }






    private fun performSearch(query: String) {
        val call = itunesServiceApi.search(query).apply {
            request().newBuilder().header("Cache-Control", "no-cache").build()
        }

        if(editText.text.isNotEmpty()){
            errorInternetFrameLayout.visibility = View.GONE
            notFoundFrameLayout.visibility = View.GONE
            historyRecyclerView.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            recyclerViewId.visibility = View.GONE
            youSearchId.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            call.enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(call: Call<ItunesResponse>, response: Response<ItunesResponse>) {
                    Log.d("ProgressBar", "Hiding progress bar after response")
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body()?.resultCount != 0) {
                        trackList.clear()
                        val tracks = response.body()?.results
                        tracks?.forEach {
                            val trackName = it.trackName
                            val artistName = it.artistName
                            val time = toMinuteMillisec(it.trackTimeMillis)
                            val url = it.artworkUrl100
                            val trackId = it.trackId
                            val collectionName = it.collectionName
                            val releaseDate = it.releaseDate
                            val primaryGenreName = it.primaryGenreName
                            val country = it.country
                            val preview = it.previewUrl
                            trackList.add(Track(trackId,trackName, artistName, time, url,collectionName,releaseDate,primaryGenreName,country,preview))
                            trackAdapter.notifyDataSetChanged()
                        }
                        notFoundFrameLayout.visibility = View.GONE
                        recyclerViewId.visibility = View.VISIBLE
                    }
                    else if (response.isSuccessful && response.body()?.resultCount == 0) {
                        recyclerViewId.visibility = View.GONE
                        notFoundFrameLayout.visibility = View.VISIBLE
                    }
                    else if (response.code() != 200) {
                        recyclerViewId.visibility = View.GONE
                        errorInternetFrameLayout.visibility = View.VISIBLE
                    }

                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        errorInternetFrameLayout.visibility = View.VISIBLE
                }
            })
        }
        else{
            recyclerViewId.visibility = View.GONE
            errorInternetFrameLayout.visibility = View.GONE
            notFoundFrameLayout.visibility = View.GONE
        }


    }

    override fun onClick(track: Track) {
        searchHistory.addTrack(historyTrackList,track)
        historyTrackAdapter.notifyDataSetChanged()

        if(clickDebounce()){
            val gsonTrack = Gson().toJson(track)
            val playerIntent = Intent(this,PlayerActivity::class.java)
            playerIntent.putExtra("TRACK",gsonTrack)
            startActivity(playerIntent)
        }


    }


}