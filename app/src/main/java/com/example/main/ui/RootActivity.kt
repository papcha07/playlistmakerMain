package com.example.main.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.ActivityRootBinding
import com.example.search.ui.SearchFragment
import com.example.settings.ui.SettingsFragment

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        changeBottomColorState()


        navController.addOnDestinationChangedListener{
            _, destination, _ ->
            when(destination.id){
                R.id.createPlaylistFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
            }
        }


    }

    private fun changeBottomColorState(){
        val bottomNavBackground = binding.bottomNavigationView
        val activeColor = ContextCompat.getColor(this, R.color.selected_color)
        val inActiveColor = ContextCompat.getColor(this, R.color.text_and_icon_nav_color)


        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )

        val colors = intArrayOf(activeColor, inActiveColor)
        val colorStateList = ColorStateList(states, colors)

        bottomNavBackground.itemIconTintList = colorStateList
        bottomNavBackground.itemTextColor = colorStateList


    }


}

