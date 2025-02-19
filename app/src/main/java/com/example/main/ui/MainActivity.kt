package com.example.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmakermain.R
import com.example.media.MediaActivity
import com.example.search.ui.SearchActivity
import com.example.settings.ui.SettingsActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val searchButtonId = findViewById<Button>(R.id.search)
        val mediaButtonId = findViewById<Button>(R.id.media)
        val settingsButtonId = findViewById<Button>(R.id.settings)


        searchButtonId.setOnClickListener{

            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)

        }

        val mediaButtonClickListener: View.OnClickListener = object : View.OnClickListener{

            override fun onClick(v: View?) {
                val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
                startActivity(mediaIntent)
            }



        }
        mediaButtonId.setOnClickListener(mediaButtonClickListener)


        settingsButtonId.setOnClickListener{


            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)


        }











    }
}