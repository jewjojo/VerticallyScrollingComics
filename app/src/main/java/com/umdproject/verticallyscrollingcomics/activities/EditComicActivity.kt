package com.umdproject.verticallyscrollingcomics.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.JsonReader
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.adapters.EditorPanelAdapter
import com.umdproject.verticallyscrollingcomics.dataClasses.ComicPanel
import com.umdproject.verticallyscrollingcomics.databinding.EditComicActivityBinding
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


// check GraphicsPaint in class repo to paint toolbar
class EditComicActivity : AppCompatActivity() {
    private lateinit var viewModel: CurrentComicViewModel
    private lateinit var filePath: String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var epAdapter: EditorPanelAdapter

    private val testPanels: MutableList<Bitmap> = mutableListOf(Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565),
        Bitmap.createBitmap(500,500, Bitmap.Config.ALPHA_8), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565),
        Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EditComicActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]

        binding.pickImageButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.panelSpacingButton.setOnClickListener() {
            val dialogBuilder = AlertDialog.Builder(this)
            val seekBar = SeekBar(this)
            seekBar.max = 100
            seekBar.progress = viewModel.panelSpacing.value!!
            dialogBuilder.setPositiveButton("Exit", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
            dialogBuilder.setTitle("Set Panel Spacing Height")
            dialogBuilder.setView(seekBar)
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.newPanelSpacing(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })

            val spacingDialog = dialogBuilder.create()
            spacingDialog.show()
        }

        binding.scrollSpeedButton.setOnClickListener() {
            val dialogBuilder = AlertDialog.Builder(this)
            val seekBar = SeekBar(this)
            seekBar.max = 100
            seekBar.progress = (viewModel.scrollSpeed.value!!*100).toInt()
            dialogBuilder.setPositiveButton("Exit", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
            dialogBuilder.setTitle("Set Scroll Speed")
            dialogBuilder.setView(seekBar)
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.setScrollSpeed((progress / 100).toFloat())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })

            val speedDialog = dialogBuilder.create()
            speedDialog.show()
        }

        binding.metadata.setOnClickListener() {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setCancelable(false)
            val metadataDialogLayout = layoutInflater.inflate(R.layout.save_comic_dialog, null)
            metadataDialogLayout.findViewById<EditText>(R.id.comic_author).setText(viewModel.author.value!!, TextView.BufferType.EDITABLE)
            metadataDialogLayout.findViewById<EditText>(R.id.comic_title).setText(viewModel.title.value!!, TextView.BufferType.EDITABLE)
            dialogBuilder.setView(metadataDialogLayout)
            dialogBuilder.setPositiveButton("Exit", DialogInterface.OnClickListener {
                    dialog, id ->
                viewModel.setAuthor(metadataDialogLayout.findViewById<EditText>(R.id.comic_author).text.toString())
                viewModel.setTitle(metadataDialogLayout.findViewById<EditText>(R.id.comic_title).text.toString())
                dialog.cancel()
            })

            val metaDialog = dialogBuilder.create()
            metaDialog.show()
        }






        mRecyclerView = findViewById(R.id.editorRecyclerView)

        if (intent.hasExtra("filePath")) {
            filePath = intent.getStringExtra("filePath")!!
            populateExistingData()
        }

        // save and exit
        binding.buttonSaveAndExit.setOnClickListener {
            // save work before exit
            finish()
        }

        mRecyclerView.layoutManager = GridLayoutManager(
            this, 3
        )


        epAdapter = EditorPanelAdapter(this, viewModel)
        mRecyclerView.adapter = epAdapter

        viewModel.panels.observe(this) {
            Log.d("PICK_IMAGE", "LiveData changed, panel size is now " + it.size)
            //epAdapter.mPanels = it
            epAdapter.notifyItemInserted(it.size)
        }


        // Helper class for creating swipe to dismiss and drag and drop
        // functionality
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Get the from and to positions.
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                // Swap the items and notify the adapter.
                viewModel.panels.value!![from] = viewModel.panels.value!![to].also { viewModel.panels.value!![to] = viewModel.panels.value!![from] }
                //Log.d("VSC_SWAP_PANELS", to.toString() + " to " + from.toString())
                epAdapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })

        // Attach the helper to the RecyclerView.

        // Attach the helper to the RecyclerView.
        helper.attachToRecyclerView(mRecyclerView)
    }

    private fun populateExistingData() {
        val comDir = File(filePath)

        val newList: MutableList<Pair<Int, Bitmap>> = mutableListOf()
        var title: String = ""
        var author: String = ""

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
                viewModel.setTitle(title)
                viewModel.setAuthor(author)
            }
        }
        val sortedList = newList.sortedWith(compareBy({it.first}))
        var finalMutableList: MutableList<ComicPanel> = mutableListOf()
        for (item in sortedList) {
            finalMutableList.add(ComicPanel(item.second))
        }
        viewModel.setPanels(finalMutableList)
    }

    fun showPanelSpacingDialog() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            val imageURI = data?.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageURI)
            Log.d("PICK_IMAGE", viewModel.panels.value?.size.toString())
            var oldList = viewModel.panels.value!!
            oldList.add(ComicPanel(bitmap))
            viewModel.setPanels(oldList)
            Log.d("PICK_IMAGE", viewModel.panels.value?.size.toString())
        }
    }


}
