package com.example.player.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.media.domain.api.PlayList
import com.example.media.ui.playlist.CreatePlaylistFragment
import com.example.media.ui.playlist.CreatePlaylistViewModel
import com.example.media.ui.playlist.PlayListScreenState
import com.example.media.ui.playlist.PlaylistFragment
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.ActivityMediaPlayerBinding
import com.example.playlistmakermain.databinding.FragmentPlayerBinding
import com.example.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerFragment : Fragment() {

    private lateinit var playButton: ImageButton
    private lateinit var timeTextView: TextView
    private var url = ""
    private val gson: Gson by inject()
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var track: Track
    private lateinit var playerViewModel: PlayerViewModel
    private val playlistViewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var adapter: BottomAdapter
    private lateinit var recyclerView: RecyclerView
    private var playListName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = PlayerFragmentArgs.fromBundle(requireArguments())
        val stringTrack = arg.track
        Log.d("stringTrack", stringTrack)
        track = gson.fromJson(stringTrack, Track::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.getAllPlayLists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetContainer = binding.playlistsBottomSheet
        val bottomBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        adapter = BottomAdapter(mutableListOf()) { playList ->
            playListName = playList.name
            addTrackInPlayList(track, playList)
        }

        recyclerView = binding.recyclerViewId
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        playlistViewModel.getAlbumState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayListScreenState.EmptyList -> {
                    showEmptyMessage()
                }

                is PlayListScreenState.Content -> {
                    showRecyclerView(state.data)
                }
            }
        }

        playlistViewModel.getAddedState().observe(viewLifecycleOwner) { state ->

            when (state) {
                true -> {
                    showAddMessage()
                    bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    playlistViewModel.clearAddedState()

                }

                false -> {
                    showAlreadyAddMessage()
                    playlistViewModel.clearAddedState()
                }

                null -> {}
            }
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

        binding.playListAddButtonId.setOnClickListener {
            bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        timeTextView = view.findViewById(R.id.currentTrackTimeId)
        playButton = view.findViewById(R.id.playButtonId)
        fillPlayer(track)

        playButton.setOnClickListener {
            togglePlayback()
        }

        playerViewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayerActivityState.Complete -> {
                    binding.playButtonId.setImageResource(R.drawable.play)
                    binding.currentTrackTimeId.setText("00:00")
                }

                else -> {}
            }
        }


        playerViewModel.getCurrentTimeState().observe(viewLifecycleOwner) { currentTime ->
            binding.currentTrackTimeId.setText(currentTime)
        }


        toggleLikeButton(track)

        backToSearch()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    playerViewModel.pause()
                    findNavController().popBackStack()
                }
            })

        binding.newPlayListButtonId.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
        }

    }

    private fun showEmptyMessage() {
        binding.recyclerViewId.visibility = View.GONE
    }

    private fun showRecyclerView(data: List<PlayList>) {
        adapter.setContent(data)
        binding.recyclerViewId.visibility = View.VISIBLE
    }


    override fun onStop() {
        super.onStop()
        playerViewModel.pause()
    }


    private fun backToSearch() {
        binding.backButtonMenu.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun fillPlayer(track: Track) {
        val trackInfo = track
        val posterId = view?.findViewById<ImageView>(R.id.posterId)

        Glide.with(this).load(trackInfo.getCoverArtwork()).placeholder(R.drawable.placeholder)
            .centerCrop().transform(
                RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.album_corner_radius))
            ).into(posterId!!)

        url = trackInfo.previewUrl!!
        playerViewModel = get { parametersOf(url) }


        binding.trackNameId.text = trackInfo.trackName
        binding.groupId.text = trackInfo.artistName
        binding.albomValueId.text = trackInfo.collectionName ?: ""
        binding.timeValueId.text = trackInfo.trackTimeMillis
        binding.yearValueId.text = trackInfo.releaseDate?.substring(0, 4)
        binding.styleValueId.text = trackInfo.primaryGenreName
        binding.countryValueId.text = trackInfo.country
        playerViewModel.initStatus(trackInfo)
        Log.d("TRACKSTATUS", "${trackInfo.isFavorite}")

        playerViewModel.getTrackStatus().observe(viewLifecycleOwner) { status ->
            when (status) {
                is TrackFavoriteState.isFavorite -> {
                    binding.likeButtonId.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.isfav
                        )
                    )
                }

                is TrackFavoriteState.isNotFavorite -> {
                    binding.likeButtonId.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.like
                        )
                    )
                }
            }
        }

    }

    private fun togglePlayback() {
        if (playerViewModel.getState().value == PlayerActivityState.Play) {
            playerViewModel.pause()
            binding.playButtonId.setImageResource(R.drawable.play)
        } else {
            playerViewModel.play()
            binding.playButtonId.setImageResource(R.drawable.pause)
        }
    }

    private fun toggleLikeButton(track: Track) {
        binding.likeButtonId.setOnClickListener {
            when {
                track.isFavorite -> {
                    deleteTrack(track)
                    track.isFavorite = false
                    binding.likeButtonId.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.like
                        )
                    )
                }

                !track.isFavorite -> {
                    addTrack(track)
                    track.isFavorite = true
                    binding.likeButtonId.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.isfav
                        )
                    )
                }
            }
        }
    }

    private fun deleteTrack(track: Track) {
        playerViewModel.deleteTrackFromFavorite(track)
    }

    private fun addTrack(track: Track) {
        playerViewModel.addTrackToFavorite(track)
    }

    private fun addTrackInPlayList(track: Track, playList: PlayList) {
        playlistViewModel.addTrackInPlayList(track, playList)
        playlistViewModel.getPlayListById(playList.id)
    }

    private fun showAlreadyAddMessage() {
        Toast.makeText(
            requireContext(),
            "Трек уже добавлен в плейлист ${playListName}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showAddMessage() {
        Toast.makeText(
            requireContext(),
            "Добавлено в плейлист ${playListName}",
            Toast.LENGTH_SHORT
        ).show()
    }
}