package com.example.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.player.ui.PlayerActivity
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.FragmentSearchBinding
import com.example.search.domain.model.Track
import com.example.search.history.ui.HistoryViewModel
import com.example.search.history.ui.TrackActivityState
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(), TrackAdapter.TrackListener {


    private lateinit var binding: FragmentSearchBinding



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
    private val trackViewModel : HistoryViewModel by viewModel()

    //search viewmodel
    private val searchViewModel : SearchViewModel by viewModel()

    //shared viewmodel

    private val gson: Gson by inject()


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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyTrackAdapter = TrackAdapter(mutableListOf(),this)

        trackViewModel.getState().observe(viewLifecycleOwner) {
                state ->
            when (state) {
                is TrackActivityState.Content -> {
                    if (state.data.isNotEmpty()) {
                        historyTrackAdapter.updateData(state.data)
                    }
                }
            }
        }

        searchViewModel.getState().observe(viewLifecycleOwner){
                state ->

            if(!editText.text.isNullOrEmpty()){
                progressBar.visibility = View.GONE
                when(state){
                    is SearchActivityState.Loading -> showLoading()
                    is SearchActivityState.Content -> showContent(state.data)
                    is SearchActivityState.Error -> showError()
                    else -> showNotFound()
                }
            }
        }



        progressBar = binding.progressBarId
        //адаптер
        recyclerViewId = binding.trackList
        trackAdapter = TrackAdapter(trackList,this)
        recyclerViewId.adapter = trackAdapter
        //история
        historyRecyclerView = binding.historyRecyclerViewId
        clearHistoryButton = binding.clearHistoryButtonId
        historyRecyclerView.adapter = historyTrackAdapter
        youSearchId = binding.youSearhId
        //заглушки
        notFoundFrameLayout = binding.notFoundId
        errorInternetFrameLayout = binding.errorInternetId
        refreshButton = binding.refreshButtonId
        //элементы editText
        editText = binding.searchId
        closeButton = binding.clearButtonId


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                progressBar.visibility = View.GONE
                if (editText.hasFocus() && (editText.text.isNullOrEmpty() || editText.text.toString() == "") && trackViewModel.getCurrentCountTrack() != 0) {
                    showHistory()
                }
                else {
                    notFoundFrameLayout.visibility = View.GONE
                    hideHistory()
                    searchDebounce()
                }

                if(s.isNullOrEmpty()){
                    closeButton.visibility = View.GONE
                }
                else{
                    closeButton.visibility = View.VISIBLE
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



    }


    private fun showContent(data: MutableList<Track>) {
        notFoundFrameLayout.visibility = View.GONE
        errorInternetFrameLayout.visibility = View.GONE
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



    private fun showKeyboard(view: View) {
        view.requestFocus()
        val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun closeKeyboard(view: View) {
        val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
        notFoundFrameLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        recyclerViewId.visibility = View.GONE
        errorInternetFrameLayout.visibility = View.VISIBLE
    }

    private fun showNotFound(){
        errorInternetFrameLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        recyclerViewId.visibility = View.GONE
        notFoundFrameLayout.visibility = View.VISIBLE
    }

    override fun onClick(track: Track) {
        trackViewModel.addTrack(track)

        if(clickDebounce()){
            val gsonTrack = gson.toJson(track)
            val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
            playerIntent.putExtra("TRACK",gsonTrack)
            startActivity(playerIntent)

        }
    }

}