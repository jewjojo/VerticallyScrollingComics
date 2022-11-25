package com.umdproject.verticallyscrollingcomics.activities

import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.ReadCommentsBinding
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel


// check GraphicsPaint in class repo to paint toolbar
class ReadComments : AppCompatActivity() {
    private lateinit var comicViewModel: CurrentComicViewModel
    private lateinit var accountViewModel: MainViewModel
    private lateinit var commentDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReadCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        comicViewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        commentDisplay = binding.readCommentsList

        // set comic title from intent
        binding.editorTitleText.text = comicViewModel.title.toString()

        // fill in list of strings
        populateScreen()

        // exit the comments page
        binding.buttonSaveAndExit.setOnClickListener {
            finish()
        }

        binding.leaveComment.setOnClickListener {
            val yourComment: String = binding.comment.text.toString()

            //check if signed in
            if (accountViewModel.email.value.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    getString(R.string.sign_in_toast),
                    Toast.LENGTH_LONG
                ).show()
            }

            if (!TextUtils.isEmpty(yourComment)) {
                leaveComment(yourComment)
            }

        }
    }

    private fun populateScreen() {
        var tempList = comicViewModel.commentsList.value
        commentDisplay.text = tempList?.joinToString(separator = "\n")
        commentDisplay.movementMethod = ScrollingMovementMethod()
    }

    private fun leaveComment(yourComment: String) {
        // add account name: comment to list and update on firebase
        val i = Log.i("Comment", "yourComment: $yourComment")

        // initialize empty list
        var tempList: MutableList<String> = arrayListOf()

        // check if comments stored in firebase are empty or dont exist
        if (comicViewModel.commentsList.value.isNullOrEmpty()) {
            // if they dont exist, initialize them to the empty list
            comicViewModel.setComments(tempList)
        }
        // copy the list from storage, add the new comment, then reupload
        tempList = comicViewModel.commentsList.value!!
        tempList.add(accountViewModel.email.value + ": " + yourComment)
        comicViewModel.setComments(tempList)

        Log.i("Comment", "All comments: $tempList")
        Log.i("Comment", "Viewmodel Storage: " + comicViewModel.commentsList.value)
        populateScreen()
    }
}
