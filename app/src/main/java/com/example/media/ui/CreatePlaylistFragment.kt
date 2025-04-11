package com.example.media.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.playlistmakermain.R
import com.example.playlistmakermain.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylistFragment : Fragment() {

    private lateinit var binding: FragmentCreatePlaylistBinding
    private val playlistViewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var nameEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    }

    private fun observeEditText() {

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val allFilled = allFilled(
                    nameEditText.text.toString(),
                    descriptionEditText.text.toString()
                )

                binding.createButtonId.isEnabled = allFilled
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        nameEditText.addTextChangedListener(watcher)
        descriptionEditText.addTextChangedListener(watcher)

    }

    private fun allFilled(name: String, desc: String): Boolean {
        return listOf(
            name,
            desc
        ).all {
            it.isNotEmpty()
        }
    }


}