package com.umdproject.verticallyscrollingcomics.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.ReadComicBinding
import com.umdproject.verticallyscrollingcomics.databinding.ReadCommentsBinding
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel


// check GraphicsPaint in class repo to paint toolbar
class ReadComic : AppCompatActivity() {
    private lateinit var comicViewModel: CurrentComicViewModel
    private lateinit var accountViewModel: MainViewModel
    private lateinit var comicID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReadComicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        comicViewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // set comic title from intent
        binding.readerTitleText.text = comicViewModel.title.toString()

        if (intent.hasExtra("comicID")) {
            comicID = intent.getStringExtra("comicID")!!
        }

        // exit the comic page
        binding.buttonClose.setOnClickListener {

            finish()
        }

        binding.buttonComments.setOnClickListener {
            val intent = Intent(this, ReadComments::class.java)
            intent.putExtra("comicID", comicID)
            startActivity(intent)
        }
    }
}
