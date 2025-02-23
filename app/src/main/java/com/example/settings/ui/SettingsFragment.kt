package com.example.settings.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.App
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.FragmentFavoriteBinding
import com.example.playlistmakermain.databinding.FragmentSettingsBinding
import com.example.sharing.domain.model.ShareDataInfo
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel : SettingsViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()


        val themeSwitcher: SwitchMaterial = binding.themeSwitcher

        themeSwitcher.isChecked = settingsViewModel.getCurrentTheme()
        settingsViewModel.getState().observe(viewLifecycleOwner){
                theme ->
            when(theme){
                true ->{
                    themeSwitcher.trackTintList = ContextCompat.getColorStateList(requireContext(),
                        R.color.back_switcher_color
                    )
                }
                else ->{
                    themeSwitcher.thumbTintList = ContextCompat.getColorStateList(requireContext()  ,
                        R.color.switcher_color
                    )
                }
            }

        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->

            (requireContext().applicationContext as App).savedTheme(checked)
            (requireContext().applicationContext as App).switchTheme(checked)

        }

    }


    private fun onClickListener(){

        binding.shareButton.setOnClickListener {
            settingsViewModel.shareLink(getString(R.string.android_course_url))
        }

        binding.reportButton.setOnClickListener {
            settingsViewModel.sendMessageToDeveloper(
                ShareDataInfo(
                    "legoman056@gmail.com",
                    getString(R.string.subject_text),
                    getString(R.string.text_to_developers))
            )
        }

        binding.documentsId.setOnClickListener {
            settingsViewModel.openLink(getString(R.string.offer))
        }
    }

}