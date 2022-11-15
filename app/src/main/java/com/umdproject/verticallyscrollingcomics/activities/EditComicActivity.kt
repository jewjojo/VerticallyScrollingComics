package com.umdproject.verticallyscrollingcomics.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.adapters.EditorPanelAdapter
import com.umdproject.verticallyscrollingcomics.databinding.EditComicActivityBinding
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import java.util.*

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

        mRecyclerView = findViewById(R.id.editorRecyclerView)

        if (intent.hasExtra("filePath")) {
            filePath = intent.getStringExtra("filePath")!!
        }

        // save and exit
        binding.buttonSaveAndExit.setOnClickListener {
            // save work before exit
            finish()
        }

        mRecyclerView.layoutManager = GridLayoutManager(
            this, 3
        )

        viewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]

        viewModel.setPanels(testPanels)

        epAdapter = EditorPanelAdapter(this, testPanels)
        mRecyclerView.adapter = epAdapter


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
                Collections.swap(viewModel.panels.value, from, to)
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
}
