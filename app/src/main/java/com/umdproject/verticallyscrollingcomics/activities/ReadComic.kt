package com.umdproject.verticallyscrollingcomics.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umdproject.verticallyscrollingcomics.adapters.ReadComicAdapter
import com.umdproject.verticallyscrollingcomics.dataClasses.ComicPanel
import com.umdproject.verticallyscrollingcomics.databinding.ReadComicBinding
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


// check GraphicsPaint in class repo to paint toolbar
class ReadComic : AppCompatActivity() {
    private lateinit var viewModel: CurrentComicViewModel
    private lateinit var comicID: String
    private lateinit var filePath: String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var readComicAdapter: ReadComicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReadComicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]

        if (intent.hasExtra("comicID")) {
            comicID = intent.getStringExtra("comicID")!!
        }
        if (intent.hasExtra("filePath")) {
            filePath = intent.getStringExtra("filePath")!!
        }

        populateExistingData()

        mRecyclerView = binding.readerComicPanels
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = ReadComicAdapter(this, viewModel)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position: Int = (mRecyclerView.layoutManager as LinearLayoutManager?)!!
                        .findFirstVisibleItemPosition()
                    // Handle haptic feedback if position has haptics
                }
            }
        })

        // set comic title
        binding.readerTitleText.text = viewModel.title.value!!
        // Set background color
        binding.bgColorView.setBackgroundColor(Color.rgb(viewModel.backgroundColor.value!!.red().toInt(), viewModel.backgroundColor.value!!.green().toInt(), viewModel.backgroundColor.value!!.blue().toInt()))









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


    private fun populateExistingData() {
        val comDir = File(filePath)

        val newList: MutableList<Pair<Int, Bitmap>> = mutableListOf()
        var title: String = ""
        var author: String = ""
        var scrollSpeed: Float = 0.0f
        var panelSpacing: Int = 0
        var bgRed: Float = 0f
        var bgGreen: Float = 0f
        var bgBlue: Float = 0f
        var hapticsPrefs: MutableList<Boolean> = mutableListOf()

        comDir.walk().forEach {
            val currFilePath = it.toString()
            Log.d("IMPORTING", it.nameWithoutExtension)


            if (it.extension == "png") {
                val pageNumber = it.nameWithoutExtension.toInt()
                newList.add(Pair(pageNumber, BitmapFactory.decodeFile(currFilePath)))
            } else if (it.name == "metadata.json") {
                val fileInputStream = FileInputStream(currFilePath)
                val jsonReader = JsonReader(InputStreamReader(fileInputStream, "UTF-8"))

                try {

                    jsonReader.beginObject()
                    while (jsonReader.hasNext()) {
                        val tokenName = jsonReader.nextName()
                        if (tokenName.equals("title")) {
                            title = jsonReader.nextString()
                        } else if (tokenName.equals("author")) {
                            author = jsonReader.nextString()

                        } else if (tokenName.equals("bgRed")) {
                            bgRed = jsonReader.nextDouble().toFloat()
                        } else if (tokenName.equals("bgGreen")) {
                            bgGreen = jsonReader.nextDouble().toFloat()
                        } else if (tokenName.equals("bgBlue")) {
                            bgBlue = jsonReader.nextDouble().toFloat()
                        } else if (tokenName.equals("scrollSpeed")) {
                            scrollSpeed = jsonReader.nextDouble().toFloat()
                        } else if (tokenName.equals("panelSpacing")) {
                            panelSpacing = jsonReader.nextDouble().toInt()
                        } else if (tokenName.equals("hapticsPrefs")) {
                            jsonReader.beginArray()
                            while (jsonReader.hasNext()) {
                                hapticsPrefs.add(jsonReader.nextBoolean())
                            }
                            jsonReader.endArray()
                        } else {
                            jsonReader.skipValue()
                        }
                    }
                    jsonReader.endObject()
                } finally {
                    jsonReader.close()
                }
                viewModel.setTitle(title)
                viewModel.setAuthor(author)
                viewModel.newPanelSpacing(panelSpacing)
                viewModel.setScrollSpeed(scrollSpeed)
                viewModel.setBackgroundColor(Color.valueOf(bgRed, bgGreen, bgBlue))
            }
        }
        val sortedList = newList.sortedWith(compareBy({it.first}))
        var finalMutableList: MutableList<ComicPanel> = mutableListOf()
        for (item in sortedList) {
            finalMutableList.add(ComicPanel(item.second))
        }

        finalMutableList.forEachIndexed() { i, panel ->
            panel.hasHaptics = hapticsPrefs[i]
        }
        viewModel.setPanels(finalMutableList)
    }
}
