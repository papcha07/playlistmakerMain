package com.example.media.ui.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.media.domain.api.PlayList
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.FragmentPlayListViewBinding
import com.example.search.domain.model.Track
import com.example.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListViewFragment : Fragment(), TrackAdapter.TrackListener {

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentPlayListViewBinding
    private lateinit var playList: PlayList
    private val playListViewModel: CreatePlaylistViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = PlayListViewFragmentArgs.fromBundle(requireArguments())
        val playListModel = Gson().fromJson(args.albumId, PlayList::class.java)
        playList = playListModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        observeTrackList()
        fillScreen()
        goBack()

        val bottomBehaviorContainer = binding.behaviorContainerId
        binding.overlay.visibility = View.VISIBLE
        val bottomBehavior = BottomSheetBehavior.from(bottomBehaviorContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomBehavior.apply {
            isHideable = false
        }

        bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.shareBottomId.deleteBottomId.setOnClickListener {
            showDeletePlayListDialog()
        }

        shareButtonClick()
        sharePlayList(playList)
        observePlayList()
    }

    override fun onClick(track: Track) {
        val gsonTrack = Gson().toJson(track, Track::class.java)
        val action =
            PlayListViewFragmentDirections.actionPlayListViewFragmentToPlayerFragment(gsonTrack)
        findNavController().navigate(action)
    }

    override fun onLongClick(track: Track) {
        showDialog(track)
    }

    private fun showDialog(track: Track) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить трек")
            .setNegativeButton("Нет") { dialog, which ->

            }
            .setPositiveButton("Да") { dialog, which ->
                playListViewModel.deleteTrack(track, playList)
                playListViewModel.getPlayListById(playList.id)
            }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        playListViewModel.getPlayListById(playList.id)
        fillScreen()
    }


    private fun fillScreen() {
        val uri = playList.path
        if (uri == null) {
            binding.imageId.setImageResource(R.drawable.placeholder)
        } else {
            binding.imageId.setImageURI(uri.toUri())
        }

        binding.titleId.text = playList.name
        binding.yearId.text = playList.description ?: ""
        binding.countTrackId.text = playList.trackCount.toString() + " треков"
        fillBottomDialog()
    }

    private fun fillBottomDialog(){
        val uri = playList.path
        if (uri == null) {
            binding.shareBottomId.playlistImage.setImageResource(R.drawable.placeholder)
        } else {
            binding.shareBottomId.playlistImage.setImageURI(uri.toUri())
        }

        binding.shareBottomId.playlistName.text = playList.name
        binding.shareBottomId.tracksCount.text = playList.trackCount.toString() + " треков"
    }

    private fun goBack() {
        binding.backButtonId.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRv() {
        trackAdapter = TrackAdapter(mutableListOf(), this)
        recyclerView = binding.recyclerViewId
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = trackAdapter
    }

    private fun observeTrackList() {
        playListViewModel.getTracksByPlayListId(playList.id)

        playListViewModel.getTrackState().observe(viewLifecycleOwner) { state ->
            trackAdapter.updateData(state.toMutableList())
        }
    }

    private fun deletePlaylist() {
        playListViewModel.deletePlayList(playList.id)
        findNavController().popBackStack()
    }

    private fun showDeletePlayListDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                "Хотите удалить плейлист ${playList.name}?"
            )
            .setPositiveButton("Да") { which, dialog ->
                deletePlaylist()
            }
            .setNegativeButton("Нет") { which, dialog ->
            }
        dialog.show()
    }

    private fun shareButtonClick() {
        binding.shareId.setOnClickListener {

            if (playList.trackCount != 0) {

            } else {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sharePlayList(playList: PlayList){
        binding.shareBottomId.shareBottomId.setOnClickListener {
            playListViewModel.shareTrackList(playList)

        }
    }

    private fun observePlayList(){
        playListViewModel.getMainScreenState().observe(viewLifecycleOwner){
            playlist ->
            playList = playList
            fillScreen()
        }
    }




}