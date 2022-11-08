package com.umdproject.verticallyscrollingcomics.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.provider.FirebaseInitProvider
import com.umdproject.verticallyscrollingcomics.databinding.EditComicActivityBinding
import com.umdproject.verticallyscrollingcomics.databinding.HomePageBinding
import com.umdproject.verticallyscrollingcomics.ui.fragments.AccountFragment
import com.umdproject.verticallyscrollingcomics.ui.fragments.BrowseFragment
import com.umdproject.verticallyscrollingcomics.ui.fragments.ContinueReadingFragment
import com.umdproject.verticallyscrollingcomics.ui.fragments.CreateFragment

// check GraphicsPaint in class repo to paint toolbar
class EditComicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EditComicActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}
