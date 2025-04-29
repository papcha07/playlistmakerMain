package com.example.media.ui.playlist

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.util.appendPlaceholders
import com.example.media.domain.api.PlayList
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.FragmentCreatePlaylistBinding
import com.example.search.domain.model.Track
import com.google.gson.Gson
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlayListFragment : CreatePlaylistFragment() {

    private val args: EditPlayListFragmentArgs by navArgs()
    private var playlistId: Int  = -1
    private val viewModel: EditPlayListViewModel by viewModel()
    private lateinit var playList: PlayList
    override var uriCheck: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistId = args.myNumber
        viewModel.loadData(playlistId)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButtonId.setText("Сохранить")
        viewModel.getPlaylistData.observe(viewLifecycleOwner){
            newplaylist ->
            fillScreen(newplaylist)
            playList = newplaylist
        }

        binding.createButtonId.setOnClickListener {
            updatePlaylist()
        }

        binding.playlistBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }


    private fun fillScreen(playList: PlayList) {
        if (playList.path.isNullOrEmpty()) {
            binding.imageId.setImageResource(R.drawable.placeholder)
        } else {
            binding.imageId.setImageURI(playList.path!!.toUri())
        }
        binding.playListNameId.setText(playList.name)
        if(playList.description.isNullOrEmpty()){
            binding.descriptionId.setText("")
        }else{
            binding.descriptionId.setText(playList.description)
        }
    }


    fun updatePlaylist() {
        val name = binding.playListNameId.text.toString()
        val description = binding.descriptionId.text.toString()
        val oldPath = playList.path


        val updatedPlaylist = PlayList(
            id = playList.id,
            name = name,
            description = description,
            path = uriCheck?.toString() ?: oldPath,
            trackList = playList.trackList,
            trackCount = playList.trackCount,
        )

        viewModel.updatePlayList(updatedPlaylist)
        findNavController().popBackStack()
    }





}