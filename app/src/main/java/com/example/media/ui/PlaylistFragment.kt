package com.example.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmakermain.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(){

    companion object {
        fun newInstance() = PlaylistFragment()
    }
    private lateinit var binding: FragmentPlaylistBinding
    private val playlistViewModel : PlaylistViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}