package com.umdproject.verticallyscrollingcomics.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import android.view.View
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
import com.google.firebase.auth.FirebaseAuth
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.adapters.EditorPanelAdapter
import com.umdproject.verticallyscrollingcomics.dataClasses.ComicPanel
import com.umdproject.verticallyscrollingcomics.databinding.EditComicActivityBinding
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.properties.Delegates
import kotlin.random.Random.Default.nextInt


// check GraphicsPaint in class repo to paint toolbar
class EditComicActivity : AppCompatActivity() {
    private lateinit var viewModel: CurrentComicViewModel
    private lateinit var filePath: String
    private var uid by Delegates.notNull<String>()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var epAdapter: EditorPanelAdapter
    private var isExisting by Delegates.notNull<Boolean>()

    private lateinit var auth: FirebaseAuth

    private val testPanels: MutableList<Bitmap> = mutableListOf(Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565),
        Bitmap.createBitmap(500,500, Bitmap.Config.ALPHA_8), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565),
        Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EditComicActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]

        auth = FirebaseAuth.getInstance()

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

                    viewModel.setScrollSpeed((progress.toFloat() / 100))
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

        binding.backgroundColorButton.setOnClickListener() {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setCancelable(false)
            val colorDialogLayout = layoutInflater.inflate(R.layout.color_picker, null)
            colorDialogLayout.findViewById<SeekBar>(R.id.blue_bar).progress = viewModel.backgroundColor.value!!.blue().toInt()
            colorDialogLayout.findViewById<SeekBar>(R.id.red_bar).progress = viewModel.backgroundColor.value!!.red().toInt()
            colorDialogLayout.findViewById<SeekBar>(R.id.green_bar).progress = viewModel.backgroundColor.value!!.green().toInt()
            colorDialogLayout.findViewById<View>(R.id.color_view).setBackgroundColor(Color.rgb(viewModel.backgroundColor.value!!.red().toInt(), viewModel.backgroundColor.value!!.green().toInt(), viewModel.backgroundColor.value!!.blue().toInt()))
            dialogBuilder.setView(colorDialogLayout)
            colorDialogLayout.findViewById<SeekBar>(R.id.blue_bar).setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        colorDialogLayout.findViewById<View>(R.id.color_view).setBackgroundColor(Color.rgb(colorDialogLayout.findViewById<SeekBar>(R.id.red_bar).progress, colorDialogLayout.findViewById<SeekBar>(R.id.green_bar).progress, colorDialogLayout.findViewById<SeekBar>(R.id.blue_bar).progress))
                    }
                    //Not Implemented
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                })
            colorDialogLayout.findViewById<SeekBar>(R.id.green_bar).setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        colorDialogLayout.findViewById<View>(R.id.color_view).setBackgroundColor(Color.rgb(colorDialogLayout.findViewById<SeekBar>(R.id.red_bar).progress, colorDialogLayout.findViewById<SeekBar>(R.id.green_bar).progress, colorDialogLayout.findViewById<SeekBar>(R.id.blue_bar).progress))
                    }
                    //Not Implemented
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                })

            colorDialogLayout.findViewById<SeekBar>(R.id.red_bar).setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        colorDialogLayout.findViewById<View>(R.id.color_view).setBackgroundColor(Color.rgb(colorDialogLayout.findViewById<SeekBar>(R.id.red_bar).progress, colorDialogLayout.findViewById<SeekBar>(R.id.green_bar).progress, colorDialogLayout.findViewById<SeekBar>(R.id.blue_bar).progress))
                    }
                    //Not Implemented
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                })


            dialogBuilder.setPositiveButton("Exit", DialogInterface.OnClickListener {
                    dialog, id ->
                viewModel.setBackgroundColor(Color.valueOf(colorDialogLayout.findViewById<SeekBar>(R.id.red_bar).progress.toFloat(), colorDialogLayout.findViewById<SeekBar>(R.id.green_bar).progress.toFloat(), colorDialogLayout.findViewById<SeekBar>(R.id.blue_bar).progress.toFloat()))

                dialog.cancel()
            })

            val colorDialog = dialogBuilder.create()
            colorDialog.show()
        }

        mRecyclerView = findViewById(R.id.editorRecyclerView)

        if (intent.hasExtra("uid")) {
            uid = intent.getIntExtra("uid", 0).toString()
        }

        if (auth.currentUser != null) {
            uid = auth.currentUser!!.uid
        } else {
            uid = 0.toString()
        }

        if (intent.hasExtra("filePath")) {
            filePath = intent.getStringExtra("filePath")!!
            isExisting = true
            populateExistingData()
        } else {
            isExisting = false
        }

        binding.buttonSaveAndExit.setOnClickListener() {
            if (viewModel.author.value!! == "" || viewModel.title.value!! == "" || viewModel.panels.value!!.size == 0) {
                Toast.makeText(applicationContext, "Make sure to set an author name, comic title, and add some panels!", Toast.LENGTH_LONG).show()
            } else {
                var comDir: String

                if (isExisting) {
                    comDir = filePath
                } else {
                    comDir =
                        this.filesDir.toString() + "/comics/" + uid + "/" + String.format(
                            "%09d",
                            nextInt(1000000000)
                        )
                    File(comDir).mkdirs()
                }



                viewModel.panels.value!!.forEachIndexed { i, panel ->
                    val panelFile = File(comDir + "/" + (i + 1).toString() + ".png")
                    panelFile.createNewFile()
                    val panelOut = panelFile.outputStream()
                    var panelImg = Bitmap.createScaledBitmap(panel.image, 600, 1000, true)
                    val completed = panelImg.compress(Bitmap.CompressFormat.PNG, 100, panelOut)
                    Log.d("VSC_SAVE", completed.toString())
                    panelOut.flush()
                    panelOut.close()
                }

                val jsonMetadataStream = File(comDir + "/" + "metadata.json").outputStream()
                val jsonWriter = JsonWriter(OutputStreamWriter(jsonMetadataStream))
                jsonWriter.setIndent("  ")


                // Writing JSON

                jsonWriter.beginObject()
                jsonWriter.name("author").value(viewModel.author.value!!)
                jsonWriter.name("title").value(viewModel.title.value!!)
                jsonWriter.name("bgRed").value(viewModel.backgroundColor.value!!.red())
                jsonWriter.name("bgGreen").value(viewModel.backgroundColor.value!!.green())
                jsonWriter.name("bgBlue").value(viewModel.backgroundColor.value!!.blue())
                jsonWriter.name("scrollSpeed").value(viewModel.scrollSpeed.value!!)
                jsonWriter.name("panelSpacing").value(viewModel.panelSpacing.value!!)

                generateHapticsPrefsBooleanArr(jsonWriter)
                jsonWriter.endObject()

                jsonWriter.close()

                finish()

            }
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

                val from = viewHolder.adapterPosition
                val to = target.adapterPosition


                viewModel.panels.value!![from] = viewModel.panels.value!![to].also { viewModel.panels.value!![to] = viewModel.panels.value!![from] }
                //Log.d("VSC_SWAP_PANELS", to.toString() + " to " + from.toString())
                epAdapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })

        helper.attachToRecyclerView(mRecyclerView)
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

    private fun generateHapticsPrefsBooleanArr(writer: JsonWriter) {
        writer.name("hapticsPrefs")
        writer.beginArray()
        for (panel in viewModel.panels.value!!) {
            writer.value(panel.hasHaptics)
        }
        writer.endArray()
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
