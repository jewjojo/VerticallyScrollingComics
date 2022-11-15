package com.umdproject.verticallyscrollingcomics.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.adapters.CommentAdapter
import com.umdproject.verticallyscrollingcomics.adapters.EditorPanelAdapter
import com.umdproject.verticallyscrollingcomics.databinding.EditComicActivityBinding
import com.umdproject.verticallyscrollingcomics.databinding.ReadCommentsBinding
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel
import java.util.*
import kotlin.collections.ArrayList

// check GraphicsPaint in class repo to paint toolbar
class ReadComments : AppCompatActivity() {
    private var commentList = arrayListOf<String>()
    private lateinit var comicViewModel: CurrentComicViewModel
    private lateinit var accountViewModel: MainViewModel
    private lateinit var commentView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReadCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        comicViewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        commentView = findViewById(R.id.readCommentsList)

        // fill in list of strings
        if (intent.hasExtra("commentsList")) {
            commentList = intent.getStringArrayListExtra("commentsList")!!

            // populate screen with comments
            populateScreen()
        }

        // exit the comments page
        binding.buttonSaveAndExit.setOnClickListener {
            finish()
        }

        binding.leaveComment.setOnClickListener {
            leaveComment(binding.comment.toString())
        }
    }

    private fun populateScreen() {
        val adapter = CommentAdapter(this, commentList)
        commentView.layoutManager = LinearLayoutManager(this)
        commentView.adapter = adapter
    }

    private fun leaveComment(yourComment: String) {
        // add account name: comment to list and update on firebase
        commentList.add(accountViewModel.email.toString() + " " + yourComment)
        populateScreen()
    }
}
