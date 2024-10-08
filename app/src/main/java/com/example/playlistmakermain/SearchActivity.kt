package com.example.playlistmakermain

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapter.TrackAdapter
import com.example.ItunesApi.ItunesApi
import com.example.ItunesApi.ItunesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Queue


class SearchActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var closeButton: ImageView
    private lateinit var frameLayout: FrameLayout
    private lateinit var recyclerViewId: RecyclerView


    private lateinit var errorImageId: ImageView
    private lateinit var errorTextId: TextView
    private lateinit var refreshButtonId: Button

    private var editTextValue: String = ""

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private val itunesServiceApi = retrofit.create(ItunesApi::class.java)

    //adapter
    private val trackList: MutableList<Track> = mutableListOf()
    private var trackAdapter = TrackAdapter(trackList)




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        //адаптер
        recyclerViewId = findViewById(R.id.trackList)
        trackAdapter = TrackAdapter(trackList)
        recyclerViewId.adapter = trackAdapter

        //элементы editText
        editText = findViewById(R.id.searchId)
        closeButton = findViewById(R.id.clearButtonId)

        //Разметка связанная с результатами запроса
        frameLayout = findViewById(R.id.frameErrorId)
        errorImageId = findViewById(R.id.errorImageId)
        refreshButtonId = findViewById(R.id.refreshButtonId)
        errorTextId = findViewById(R.id.errorTextMessageId)

        //EditText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    closeButton.visibility = View.GONE
                    closeKeyboard(editText)
                } else {
                    closeButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().length == 0) {
                    clearTrackList(trackAdapter)
                }
            }
        }
        editText.addTextChangedListener(textWatcher)


        //Работа с API
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = editText.text.toString()
                performSearch(query)
                true
            }
            false
        }



        refreshButtonId.setOnClickListener {
            val lastQuery = editText.text.toString()
            performSearch(lastQuery)
        }

        editText.setOnClickListener {
            showKeyboard(editText)
        }

        closeButton.setOnClickListener {
            editText.setText("")
            closeKeyboard(editText)
            clearTrackList(trackAdapter)
        }

        val backButton = findViewById<ImageView>(R.id.searchBackButton)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }
    }

    private fun toMinuteMillisec(millisecond: String): String{
        val date = Date(millisecond.toLong())
        val format = SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)
        return format
    }

    private fun clearTrackList(adapter: TrackAdapter){
        trackList.clear()
        frameLayout.visibility = View.GONE
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


    private fun showErrorPreview(){
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        recyclerViewId.visibility = View.GONE
        frameLayout.visibility = View.VISIBLE
        when(nightModeFlags){

            Configuration.UI_MODE_NIGHT_YES ->{
                errorImageId.setImageResource(R.drawable.internet_not_found_night)
            }

            Configuration.UI_MODE_NIGHT_NO ->{
                errorImageId.setImageResource(R.drawable.internet_not_found)
            }
        }
        errorTextId.setText(R.string.internet_problems)
        refreshButtonId.visibility = View.VISIBLE
    }

    private fun showNotFoundPreview(){
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        recyclerViewId.visibility = View.GONE
        frameLayout.visibility = View.VISIBLE
        when(nightModeFlags){

            Configuration.UI_MODE_NIGHT_YES ->{
                errorImageId.setImageResource(R.drawable.music_no_found_night)
                refreshButtonId.visibility = View.GONE
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                errorImageId.setImageResource(R.drawable.music_no_found)
                refreshButtonId.visibility = View.GONE
            }
        }
        errorTextId.setText(R.string.not_found)
    }

    private fun performSearch(query: String) {
        trackList.clear()
        val call = itunesServiceApi.search(query).apply {
            request().newBuilder().header("Cache-Control", "no-cache").build()
        }
        call.enqueue(object : Callback<ItunesResponse> {
            override fun onResponse(call: Call<ItunesResponse>, response: Response<ItunesResponse>) {
                if (response.isSuccessful && response.body()?.resultCount != 0) {
                    recyclerViewId.visibility = View.VISIBLE
                    val tracks = response.body()?.results
                    tracks?.forEach {
                        val trackName = it.trackName
                        val artistName = it.artistName
                        val time = toMinuteMillisec(it.trackTimeMillis)
                        val url = it.artworkUrl100
                        trackList.add(Track(trackName, artistName, time, url))
                        trackAdapter.notifyDataSetChanged()
                        Log.i("НАШЛИСЬ ТРЕКИ","${response.body()?.resultCount}")
                    }
                } else if (response.isSuccessful && response.body()?.resultCount == 0) {
                    showNotFoundPreview()
                    Log.i("НЕ НАШЛИСЬ ТРЕКИ","${response.body()?.resultCount}")
                } else if (response.code() != 200) {
                    showErrorPreview()
                    Log.i("ОШИБКА","${response.body()?.resultCount}")
                }
            }

            override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                showErrorPreview()
                Log.i("ОШИБКА","ПРОСТО НЕТ ИНЕТА")
            }
        })
    }

}