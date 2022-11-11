package com.umdproject.verticallyscrollingcomics.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umdproject.verticallyscrollingcomics.databinding.EditComicActivityBinding

// check GraphicsPaint in class repo to paint toolbar
class EditComicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EditComicActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // save and exit
        binding.buttonSaveAndExit.setOnClickListener {
            // save work before exit
            finish()
        }
    }
}
