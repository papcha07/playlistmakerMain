package com.example.playlistmakermain

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val backButton = findViewById<ImageView>(R.id.back)

        backButton.setOnClickListener{

            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)

        }


        val shareButtonId = findViewById<LinearLayout>(R.id.shareButton)
        shareButtonId.setOnClickListener{

            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
            startActivity(sendIntent)

        }


        val reportButtonId = findViewById<LinearLayout>(R.id.reportButton)
        reportButtonId.setOnClickListener{


            val reportIntent = Intent(Intent.ACTION_SEND)
            reportIntent.setType("message/rfc822")


            reportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("legoman056@gmail.com"))
            reportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_text))
            reportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_to_developers))

            startActivity(reportIntent)
        }


        val documentsButtonId = findViewById<LinearLayout>(R.id.documentsId)

        documentsButtonId.setOnClickListener{


            val browserIntent = Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.offer)))
            startActivity(browserIntent)



        }











    }
}