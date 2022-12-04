package com.umdproject.verticallyscrollingcomics.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.provider.FirebaseInitProvider
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel
import com.umdproject.verticallyscrollingcomics.databinding.HomePageBinding
import com.umdproject.verticallyscrollingcomics.ui.fragments.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        //binding.buttonContinueReading.visibility = View.INVISIBLE

        // Splash Screen load in and hide
        //supportActionbar?.hide()

        // Initialize Firebase APIs
        FirebaseInitProvider()

        auth = FirebaseAuth.getInstance()

        // Something to do here: Set the viewModel's uid property to the user's ID. Need to do this before getting previews

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val downloadDir = File(this.filesDir, "/downloads")
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }

        populateLocalComicPreviews()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, BrowseFragment.newInstance())
                .commitNow()
        }

        // change fragment on browse press
        binding.buttonBrowse.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, BrowseFragment.newInstance())
                .commitNow()
        }

        // Removed Bookmarks button, implement at a later date
        /*
        binding.buttonContinueReading.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, ContinueReadingFragment.newInstance())
                .commitNow()
        } */

        // change fragment on Create press
        binding.buttonCreate.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, CreateFragment.newInstance())
                .commitNow()
        }

        // open Account login/register page
        binding.buttonAccount.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .replace(binding.fragmentContainerView.id, AccountFragment.newInstance())
//                .commitNow()
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        //DELETE THIS, testing readComments
        /*val intent2 = Intent(this, ReadComments::class.java)
        startActivity(intent2)*/
    }

    override fun onResume() {
        super.onResume()
        Log.d("VSC_RESUME", "Resumed main activity.")
        if (auth.currentUser != null) {
            Log.d("VSC_USER", auth.currentUser!!.uid)
        }
        populateLocalComicPreviews()

    }

    private fun populateLocalComicPreviews() {
        var currUid: String
        currUid = if (auth.currentUser != null) {
            auth.currentUser!!.uid
        } else {
            0.toString()
        }
        val uidDir = File(this.filesDir, "/comics/" + currUid)
        val uidDirDefault = File(this.filesDir, "/comics/" + 0.toString())
        if (!uidDir.exists()) {
            uidDir.mkdirs()
            return
        }

        val newList: MutableList<LocalComicPreview> = mutableListOf()

        val defaultDirectories = uidDirDefault.listFiles { obj: File -> obj.isDirectory }

        var directories = uidDir.listFiles { obj: File -> obj.isDirectory }

        /*if (defaultDirectories != null && currUid != "0") {
            directories += defaultDirectories
        }*/

        // Iterate through all comics in filepath
        for (it in directories) {
            val currDir = it.toString()

            val titleImg = BitmapFactory.decodeFile(currDir + "/1.png")

            val fileInputStream = FileInputStream(currDir + "/metadata.json")
            val jsonReader = JsonReader(InputStreamReader(fileInputStream, "UTF-8"))
            var title: String = ""
            var author: String = ""
            try {
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
        Log.d("VSC_RESUME","After refreshing, preview list is of size " + newList.size)
    }
}