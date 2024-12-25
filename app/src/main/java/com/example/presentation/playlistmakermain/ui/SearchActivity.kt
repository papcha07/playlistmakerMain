package com.example.presentation.playlistmakermain.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.Creator
import com.example.data.network.ItunesApi
import com.example.data.network.RetrofitNetworkClient
import com.example.data.repository.HistoryRepositoryImpl
import com.example.data.repository.TrackRepositoryImpl
import com.example.domain.consumer.Consumer
import com.example.domain.consumer.ConsumerData
import com.example.domain.impl.HistoryInteractorImpl
import com.example.domain.impl.TracksUseCase
import com.example.domain.model.Track
import com.example.playlistmakermain.R
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
    private lateinit var history : MutableList<Track>
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var historyInteractor: HistoryInteractorImpl

    private lateinit var tracksUseCase: TracksUseCase





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.progressBarId)


        historyInteractor = Creator.provideHistoryInteractor()
        history = historyInteractor.getHistory()
        Log.e("КОЛ-ВО ТРЕКОВ","${history.size}")


        //адаптер
        recyclerViewId = findViewById(R.id.trackList)
        trackAdapter = TrackAdapter(trackList,this)
        historyTrackAdapter = TrackAdapter(historyInteractor.getHistory(),this)
        recyclerViewId.adapter = trackAdapter

        historyRecyclerView = findViewById(R.id.historyRecyclerViewId)
        clearHistoryButton = findViewById(R.id.clearHistoryButtonId)
        historyRecyclerView.adapter = historyTrackAdapter
        youSearchId = findViewById(R.id.youSearhId)


        notFoundFrameLayout = findViewById(R.id.notFoundId)
        errorInternetFrameLayout = findViewById(R.id.errorInternetId)
        refreshButton = findViewById(R.id.refreshButtonId)

        //элементы editText
        editText = findViewById(R.id.searchId)
        closeButton = findViewById(R.id.clearButtonId)

        tracksUseCase = Creator.provideTracksUseCase()



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.hasFocus() && (editText.text.isNullOrEmpty() || editText.text.toString() == "") && historyInteractor.getHistory().isNotEmpty()) {
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
            if (hasFocus && (editText.text.isNullOrEmpty() || editText.text.toString() == "") && historyInteractor.getHistory().isNotEmpty()) {
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
        historyInteractor.clearHistory()
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

            if (query.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                recyclerViewId.visibility = View.GONE
                notFoundFrameLayout.visibility = View.GONE
                errorInternetFrameLayout.visibility = View.GONE
                youSearchId.visibility = View.GONE

                tracksUseCase.execute(query, object : Consumer<MutableList<Track>> {
                    override fun consume(data: ConsumerData<MutableList<Track>>) {
                        handler.post {
                            when (data) {
                                is ConsumerData.Data -> {
                                    trackList.clear()
                                    trackList.addAll(data.value)
                                    trackAdapter.notifyDataSetChanged()
                                    progressBar.visibility = View.GONE
                                    recyclerViewId.visibility = View.VISIBLE
                                }
                                is ConsumerData.Error -> {
                                    progressBar.visibility = View.GONE
                                    recyclerViewId.visibility = View.GONE
                                    if (data.message == "no internet" || data.message == "api-error") {
                                        Log.e("message",data.message)
                                        errorInternetFrameLayout.visibility = View.VISIBLE
                                    } else {
                                        notFoundFrameLayout.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }

                    }
                })
            } else {
                clearTrackList(trackAdapter)
            }


    }


    override fun onClick(track: Track) {
        historyInteractor.addTrack(track)
        historyTrackAdapter.updateData(historyInteractor.getHistory())
        if(clickDebounce()){
            val gsonTrack = Gson().toJson(track)
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra("TRACK",gsonTrack)
            startActivity(playerIntent)

        }
    }


}