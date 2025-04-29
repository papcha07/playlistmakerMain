package com.example.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.media.domain.api.PlayList
import com.example.media.ui.MediaFragmentDirections
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.FragmentPlaylistBinding
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(){

    companion object {
        fun newInstance() = PlaylistFragment()
    }
    private lateinit var binding: FragmentPlaylistBinding
    private val playlistViewModel : CreatePlaylistViewModel by viewModel()
    private lateinit var adapter: PlayListAdapter
    private lateinit var recyclerView: RecyclerView

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
        openCreatePlaylistFragment()

        adapter = PlayListAdapter(mutableListOf()){
            album ->
            val jsonAlbum = Gson().toJson(album, PlayList::class.java)
            val action = MediaFragmentDirections.actionMediaFragmentToPlayListViewFragment(jsonAlbum)
            findNavController().navigate(action)
        }
        recyclerView = binding.recyclerViewId
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter


        playlistViewModel.getAlbumState().observe(viewLifecycleOwner) {
            state ->
            when(state) {
                is PlayListScreenState.EmptyList -> {
                    showEmptyMessage()
                }

                is PlayListScreenState.Content -> {
                    showRecyclerView(state.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.getAllPlayLists()
    }

    private fun showEmptyMessage() {
        binding.recyclerViewId.visibility = View.GONE
        binding.iconId.visibility = View.VISIBLE
        binding.textViewId.visibility = View.VISIBLE
    }

    private fun showRecyclerView(content: List<PlayList>){
        binding.recyclerViewId.visibility = View.VISIBLE
        binding.iconId.visibility = View.GONE
        binding.textViewId.visibility = View.GONE
        adapter.setContent(content)
    }

    private fun openCreatePlaylistFragment(){
        binding.createButtonId.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }
    }




}