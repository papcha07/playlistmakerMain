package com.example.presentation.playlistmakermain.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.Creator
import com.example.presentation.playlistmakermain.App
import com.example.playlistmakermain.R
import com.example.domain.api.ThemeInteractorInterface
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var themeInteractorImpl: ThemeInteractorInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        themeInteractorImpl = Creator.provideThemeInteractor()

        val backButton = findViewById<ImageView>(R.id.back)

        backButton.setOnClickListener{
            finish()
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


        val themeSwitcher: SwitchMaterial = findViewById(R.id.themeSwitcher)

        themeSwitcher.isChecked = themeInteractorImpl.getCurrentTheme()

        if(themeSwitcher.isChecked){
            themeSwitcher.trackTintList = ContextCompat.getColorStateList(this,
                R.color.back_switcher_color
            )
            themeSwitcher.thumbTintList = ContextCompat.getColorStateList(this  ,
                R.color.switcher_color
            )
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->

            (applicationContext as App).savedTheme(checked)
            (applicationContext as App).switchTheme(checked)

        }












    }
}