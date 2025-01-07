package com.example.settings.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.App
import com.example.playlistmakermain.R
import com.example.settings.domain.api.ThemeInteractorInterface
import com.example.sharing.domain.model.ShareDataInfo
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {


    private val settingsViewModel : SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back)

        backButton.setOnClickListener{
            finish()
        }


        val shareButtonId = findViewById<LinearLayout>(R.id.shareButton)
        shareButtonId.setOnClickListener{
            settingsViewModel.shareLink(getString(R.string.android_course_url))
        }

        val reportButtonId = findViewById<LinearLayout>(R.id.reportButton)
        reportButtonId.setOnClickListener{
            settingsViewModel.sendMessageToDeveloper(
                ShareDataInfo(
                    "legoman056@gmail.com",
                    getString(R.string.subject_text),
                    getString(R.string.text_to_developers))
            )
        }

        val documentsButtonId = findViewById<LinearLayout>(R.id.documentsId)
        documentsButtonId.setOnClickListener{
            val offerLink = getString(R.string.offer)
            settingsViewModel.openLink(offerLink)
        }


        val themeSwitcher: SwitchMaterial = findViewById(R.id.themeSwitcher)

        themeSwitcher.isChecked = settingsViewModel.getCurrentTheme()
        settingsViewModel.getState().observe(this){
            theme ->
            when(theme){
                true ->{
                    themeSwitcher.trackTintList = ContextCompat.getColorStateList(this,
                        R.color.back_switcher_color
                    )
                }
                else ->{
                    themeSwitcher.thumbTintList = ContextCompat.getColorStateList(this  ,
                        R.color.switcher_color
                    )
                }
            }

        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->

            (applicationContext as App).savedTheme(checked)
            (applicationContext as App).switchTheme(checked)

        }












    }
}