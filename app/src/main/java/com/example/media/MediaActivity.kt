package com.example.media

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var mediaViewBinding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaViewBinding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(mediaViewBinding.root)


        mediaViewBinding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabLayoutMediator = TabLayoutMediator(mediaViewBinding.tabLayout, mediaViewBinding.viewPager){
            tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favotite_tracks)
                1 -> tab.text = getString(R.string.playlsit)
            }
        }
        tabLayoutMediator.attach()
        setOnClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }

    private fun setOnClickListener(){
        mediaViewBinding.mediaBckButtonId.setOnClickListener {
            this.finish()
        }
    }
}