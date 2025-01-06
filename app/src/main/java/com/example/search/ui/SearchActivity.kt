package com.example.search.ui

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.creator.Creator
import com.example.search.history.domain.api.HistoryInteractorInterface
import com.example.search.domain.model.Track
import com.example.main.ui.MainActivity
import com.example.playlistmakermain.R
import com.example.search.history.ui.HistoryViewModel
import com.example.search.history.ui.TrackActivityState
import com.example.player.ui.PlayerActivity
import com.google.gson.Gson


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



    //history viewmodel
    private val trackViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    //search viewmodel
    private val searchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
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

    //track adapter
    private val trackList: MutableList<Track> = mutableListOf()
    private var trackAdapter = TrackAdapter(trackList,this)

    //history adapter
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var historyInteractor: HistoryInteractorInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        historyTrackAdapter = TrackAdapter(mutableListOf(),this)

        trackViewModel.getState().observe(this) {
            state ->
            when (state) {
                is TrackActivityState.Content -> {
                    if (state.data.isNotEmpty()) {
                        historyTrackAdapter.updateData(state.data)
                    }
                }
            }
        }

        searchViewModel.getState().observe(this){
            state ->
            when(state){
                is SearchActivityState.Loading -> showLoading()
                is SearchActivityState.Content -> showContent(state.data)
                is SearchActivityState.Error -> showError()
                else -> showNotFound()
            }
        }



        progressBar = findViewById(R.id.progressBarId)
        historyInteractor = Creator.provideHistoryInteractor()
        //адаптер
        recyclerViewId = findViewById(R.id.trackList)
        trackAdapter = TrackAdapter(trackList,this)
        recyclerViewId.adapter = trackAdapter
        //история
        historyRecyclerView = findViewById(R.id.historyRecyclerViewId)
        clearHistoryButton = findViewById(R.id.clearHistoryButtonId)
        historyRecyclerView.adapter = historyTrackAdapter
        youSearchId = findViewById(R.id.youSearhId)
        //заглушки
        notFoundFrameLayout = findViewById(R.id.notFoundId)
        errorInternetFrameLayout = findViewById(R.id.errorInternetId)
        refreshButton = findViewById(R.id.refreshButtonId)
        //элементы editText
        editText = findViewById(R.id.searchId)
        closeButton = findViewById(R.id.clearButtonId)


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.hasFocus() && (editText.text.isNullOrEmpty() || editText.text.toString() == "") && trackViewModel.getCurrentCountTrack() != 0) {
                    showHistory()
                }
                else {
                    closeButton.visibility = View.VISIBLE
                    hideHistory()
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearTrackList(trackAdapter)
                }
                editTextValue = s.toString()
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && (editText.text.isNullOrEmpty() || editText.text.toString() == "") && trackViewModel.getCurrentCountTrack() != 0) {
                showHistory()
                Log.e("история","${historyInteractor.getHistory().size}")
            }
            else{
                hideHistory()
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

    private fun showContent(data: MutableList<Track>) {
        trackList.clear()
        trackList.addAll(data)
        trackAdapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
        recyclerViewId.visibility = View.VISIBLE
    }

    private fun clearHistoryTrackList(){
        clearHistoryButton.visibility = View.GONE
        youSearchId.visibility = View.GONE
        historyRecyclerView.visibility = View.GONE
        trackViewModel.clearTrackList()
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

    private fun showHistory(){
        recyclerViewId.visibility = View.GONE
        errorInternetFrameLayout.visibility = View.GONE
        notFoundFrameLayout.visibility = View.GONE
        youSearchId.visibility = View.VISIBLE
        historyRecyclerView.visibility = View.VISIBLE
        clearHistoryButton.visibility = View.VISIBLE
    }

    private fun hideHistory(){
        historyRecyclerView.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        youSearchId.visibility = View.GONE
    }

    private fun performSearch(query: String){
        searchViewModel.loadTrackList(query)
    }

    private fun showLoading(){
        progressBar.visibility = View.VISIBLE
        recyclerViewId.visibility = View.GONE
        notFoundFrameLayout.visibility = View.GONE
        errorInternetFrameLayout.visibility = View.GONE
        youSearchId.visibility = View.GONE
    }

    private fun showError(){
        progressBar.visibility = View.GONE
        recyclerViewId.visibility = View.GONE
        errorInternetFrameLayout.visibility = View.VISIBLE
    }

    private fun showNotFound(){
        progressBar.visibility = View.GONE
        recyclerViewId.visibility = View.GONE
        notFoundFrameLayout.visibility = View.VISIBLE
    }

    override fun onClick(track: Track) {
        trackViewModel.addTrack(track)

        if(clickDebounce()){
            val gsonTrack = Gson().toJson(track)
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra("TRACK",gsonTrack)
            startActivity(playerIntent)

        }
    }


}