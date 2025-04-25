package com.example.media.ui.playlist

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.media.domain.api.PlayList
import com.example.playlistmakermain.databinding.FragmentCreatePlaylistBinding
import com.example.search.domain.model.Track
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylistFragment : Fragment() {

    private lateinit var binding: FragmentCreatePlaylistBinding
    private val playlistViewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var nameEditText: TextInputEditText
    private var uriCheck: Uri? = null
    private lateinit var descriptionEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameEditText = binding.playListNameId
        descriptionEditText = binding.descriptionId
        observeEditText()
        setAlbumImage()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (playlistViewModel.getBackState().value == true) {
                findNavController().popBackStack()
            } else {
                showDialog()
            }
        }

        binding.playlistBackButton.setOnClickListener {
            if (playlistViewModel.getBackState().value == true) {
                findNavController().popBackStack()
            } else {
                showDialog()
            }
        }

        createPlaylist()

    }

    private fun observeEditText() {

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val allFilled = allFilled(
                    nameEditText.text.toString(),
                )

                checkForFill(nameEditText.text.toString(), descriptionEditText.text.toString())
                binding.createButtonId.isEnabled = allFilled
                binding.createButtonId.isClickable = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        nameEditText.addTextChangedListener(watcher)
        descriptionEditText.addTextChangedListener(watcher)

    }

    private fun allFilled(name: String): Boolean {
        return listOf(
            name,
        ).all {
            it.isNotEmpty()
        }
    }

    private fun setAlbumImage() {

        val photoPicker = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                binding.imageId.setImageURI(uri)
                uriCheck = uri
                checkForFill(nameEditText.text.toString(), descriptionEditText.text.toString())
            }
        }

        binding.playListImageContainerId.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }


    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Завершить") { dialog, which ->
                findNavController().popBackStack()
            }
        dialog.show()

    }


    private fun checkForFill(param1: String?, param2: String?) {
        val textCheck = listOf(
            param1,
            param2
        ).any {
            !it.isNullOrEmpty()
        } // если хотя бы одно не пустое

        val uri = uriCheck

        when {
            (textCheck || (uri != null)) -> playlistViewModel.cantOut()
            else -> playlistViewModel.canOut()
        }
    }

    private fun createPlaylist() {
        binding.createButtonId.setOnClickListener {
            playlistViewModel.savePlayList(
                PlayList(
                    id = 0,
                    name = binding.playListNameId.text.toString(),
                    description = binding.descriptionId.text.toString(),
                    path = uriCheck.toString(),
                    trackList = Gson().toJson(mutableListOf<Track>()),
                    trackCount = 0
                )
            )
            findNavController().popBackStack()
        }
    }


}