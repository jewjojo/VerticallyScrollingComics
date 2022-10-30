package com.umdproject.verticallyscrollingcomics

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.provider.FirebaseInitProvider
import com.umdproject.verticallyscrollingcomics.databinding.HomePageBinding
import com.umdproject.verticallyscrollingcomics.ui.main.AccountFragment
import com.umdproject.verticallyscrollingcomics.ui.main.BrowseFragment
import com.umdproject.verticallyscrollingcomics.ui.main.ContinueReadingFragment
import com.umdproject.verticallyscrollingcomics.ui.main.CreateFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase APIs
        FirebaseInitProvider()


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, AccountFragment.newInstance())
                .commitNow()
        }

        // change fragment on browse press
        binding.buttonBrowse.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, BrowseFragment.newInstance())
                .commitNow()
        }

        // change fragment on Continue Reading press
        binding.buttonContinueReading.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, ContinueReadingFragment.newInstance())
                .commitNow()
        }

        // change fragment on Create press
        binding.buttonCreate.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, CreateFragment.newInstance())
                .commitNow()
        }

        // change fragment on Account press
        binding.buttonAccount.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, AccountFragment.newInstance())
                .commitNow()
        }
    }
}