package com.umdproject.verticallyscrollingcomics

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.provider.FirebaseInitProvider
import com.umdproject.verticallyscrollingcomics.databinding.HomePageBinding
import com.umdproject.verticallyscrollingcomics.ui.fragments.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Splash Screen load in and hide
        //supportActionbar?.hide()

        // Initialize Firebase APIs
        FirebaseInitProvider()

        // Something to do here: Set the viewModel's uid property to the user's ID. Need to do this before getting previews

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        populateLocalComicPreviews()

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

    public fun populateLocalComicPreviews() {
        val uidDir = File(this.filesDir, "/comics/" + viewModel.uid.value.toString())
        if (!uidDir.exists()) {
            uidDir.mkdirs()
            return
        }
        val newList: MutableList<LocalComicPreview> = mutableListOf()

        val directories = uidDir.listFiles { obj: File -> obj.isDirectory }

        // Iterate through all comics in filepath
        for (it in directories) {
            val currDir = it.toString()


            val titleImg = BitmapFactory.decodeFile(currDir + "/title.png")

            val fileInputStream = FileInputStream(currDir + "/metadata.json")
            val jsonReader = JsonReader(InputStreamReader(fileInputStream, "UTF-8"))
            var title: String = ""
            var author: String = ""
            try {
                // For now the metadata is just a single JSON object with some values, this will change later
                jsonReader.beginObject()

                while (jsonReader.hasNext()) {
                    val tokenName = jsonReader.nextName()
                    if (tokenName.equals("title")) {
                        title = jsonReader.nextString()
                    } else if (tokenName.equals("author")) {
                        author = jsonReader.nextString()

                    } else {
                        jsonReader.skipValue()
                    }
                }
                jsonReader.endObject()
            } finally {
                jsonReader.close()
            }

            newList.add(LocalComicPreview(titleImg, currDir, author, title))
        }
        viewModel.setComicPreviews(newList)
    }
}