package com.example.playlistmakermain

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView


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