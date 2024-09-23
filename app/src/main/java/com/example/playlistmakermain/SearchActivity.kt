package com.example.playlistmakermain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var closeButton: ImageView
    private var editTextValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        editText = findViewById(R.id.searchId)
        closeButton = findViewById(R.id.clearButtonId)



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    closeButton.visibility = View.GONE
                    closeKeyboard(editText)
                } else {
                    closeButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                editTextValue = s.toString()
            }
        }
        editText.addTextChangedListener(textWatcher)

        editText.setOnClickListener {
            showKeyboard(editText)
        }

        closeButton.setOnClickListener {
            editText.setText("")
            closeKeyboard(editText)
        }

        val backButton = findViewById<ImageView>(R.id.searchBackButton)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_TEXT", editTextValue)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextValue = savedInstanceState.getString("SEARCH_TEXT", "")
        editText.setText(editTextValue)
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun closeKeyboard(view: View) {
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(view.windowToken, 0)
    }
}