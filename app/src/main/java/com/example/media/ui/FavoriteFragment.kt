package com.example.media.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.player.ui.PlayerActivity
import com.example.playlistmakermain.databinding.FragmentFavoriteBinding
import com.example.search.domain.model.Track
import com.example.search.ui.TrackAdapter
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() , TrackAdapter.TrackListener {

    companion object{
        fun newInstance() = FavoriteFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
    private val gson: Gson by inject()
    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel : FavoriteViewModel by viewModel()
    private var isClickAllowed = true

    private fun clickDebounce() : Boolean{
        val current = isClickAllowed
        if(current){
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.favoriteRecyclerViewId
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val trackAdapter = TrackAdapter(mutableListOf(), this)
        recyclerView.adapter = trackAdapter


        favoriteViewModel.getFavoriteTracks().observe(viewLifecycleOwner){
            state ->
            when(state) {
                is FavoriteScreenState.Content -> {
                    trackAdapter.updateData(state.data.toMutableList())
                    hideStub()
                }
                is FavoriteScreenState.Empty -> {
                    showStub()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.showTrackList()
    }

    override fun onClick(track: Track) {
        if(clickDebounce()){
            val gsonTrack = gson.toJson(track)
            val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
            playerIntent.putExtra("TRACK",gsonTrack)
            startActivity(playerIntent)
        }
    }

    private fun hideStub(){
        binding.iconId.visibility = View.GONE
        binding.mediaTextId.visibility = View.GONE
        binding.favoriteRecyclerViewId.visibility = View.VISIBLE
    }

    private fun showStub(){
        binding.iconId.visibility = View.VISIBLE
        binding.mediaTextId.visibility = View.VISIBLE
        binding.favoriteRecyclerViewId.visibility = View.GONE
    }

}