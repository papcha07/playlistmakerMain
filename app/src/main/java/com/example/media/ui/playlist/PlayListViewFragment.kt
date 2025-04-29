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
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListViewFragment : Fragment(), TrackAdapter.TrackListener {

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentPlayListViewBinding
    private lateinit var playList: PlayList
    private val playListViewModel: CreatePlaylistViewModel by viewModel()
    private val gson: Gson by inject()
    private var counterTrack = -1

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
        showEmptyPlayListMessage()
        initRv()
        observeTrackList()
        fillScreen(playList)
        goBack()

        val bottomBehaviorContainer = binding.behaviorContainerId
        binding.overlay.visibility = View.VISIBLE
        val bottomBehavior = BottomSheetBehavior.from(bottomBehaviorContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
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



        val bottomShareContainer = binding.shareBottomId.bottomLayoutId
        val bottomShareBehavior = BottomSheetBehavior.from(bottomShareContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.moreId.setOnClickListener {
            bottomShareBehavior.state  = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                    else -> {
                        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }



        })

        binding.shareBottomId.editBottomId.setOnClickListener {
            val action = PlayListViewFragmentDirections.actionPlayListViewFragmentToEditPlayListFragment(playList.id)
            findNavController().navigate(action)
        }



        binding.shareBottomId.shareBottomId.setOnClickListener {
            val current = playListViewModel.getMainScreenState().value
            if (current!!.trackCount != 0) {
                playListViewModel.shareTrackList(current)
            } else {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.shareId.setOnClickListener {
            val current = playListViewModel.getMainScreenState().value
            if (current!!.trackCount != 0) {
                playListViewModel.shareTrackList(current)
            } else {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    override fun onClick(track: Track) {
        val gsonTrack = Gson().toJson(track, Track::class.java)
        val action = PlayListViewFragmentDirections.actionPlayListViewFragmentToPlayerFragment(gsonTrack)
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



    private fun fillScreen(playList: PlayList) {
        val uri = playList.path
        if (uri == null) {
            binding.imageId.setImageResource(R.drawable.placeholder)
        } else {
            binding.imageId.setImageURI(uri.toUri())
        }

        binding.titleId.text = playList.name
        binding.yearId.text = playList.description ?: ""
        binding.countTrackId.text = playList.trackCount.toString() + " треков"

        val type = object : TypeToken<List<Track>>() {}.type
        val trackList: MutableList<Track> = gson.fromJson(playList.trackList, type) ?: mutableListOf()
        if(trackList.isNullOrEmpty()){
            binding.behaviorContainerId.visibility = View.GONE
            binding.overlay.visibility = View.GONE
        } else{
            binding.behaviorContainerId.visibility = View.VISIBLE
            binding.overlay.visibility = View.VISIBLE
        }
        trackAdapter.updateData(trackList)
        binding.minutesId.text = convertToTotalMinutes(trackList)
        fillBottomDialog(playList)
    }

    private fun fillBottomDialog(playList: PlayList){
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
        playListViewModel.getMainScreenState().observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                val type = object : TypeToken<List<Track>>() {}.type
                val tracks = gson.fromJson<List<Track>>(playList.trackList, type) ?: emptyList()
                trackAdapter.updateData(tracks.toMutableList())
                fillScreen(playlist)
                playList = it
            }
        }
//        playListViewModel.getPlayListById(playList.id)
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




    override fun onResume() {
        super.onResume()
        playListViewModel.getPlayListById(playList.id)
    }


    private fun convertToTotalMinutes(trackList: List<Track>): String {
        if (trackList.isNullOrEmpty()) {
            return "0 минут"
        }
        var totalSeconds = 0L
        trackList.forEach { track ->
            try {
                val parts = track.trackTimeMillis.split(":")
                if (parts.size == 2) {
                    val minutes = parts[0].toLong()
                    val seconds = parts[1].toLong()
                    totalSeconds += minutes * 60 + seconds
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val totalMinutes = totalSeconds / 60
        val remainingSeconds = totalSeconds % 60

        return if (remainingSeconds == 0L) {
            "$totalMinutes минут"
        } else {
            "%.0f минут".format(totalMinutes + remainingSeconds / 60.0)        }
    }

    private fun showEmptyPlayListMessage(){
        if(playList.trackCount == 0 ){
            Toast.makeText(requireContext(), "В этом плейлисте нет треков", Toast.LENGTH_SHORT).show()
        }
    }




}