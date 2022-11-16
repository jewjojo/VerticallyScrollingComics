package com.umdproject.verticallyscrollingcomics.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.ReadCommentsBinding
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel

// check GraphicsPaint in class repo to paint toolbar
class ReadComments : AppCompatActivity() {
    private var commentList = arrayListOf<String>()
    //private lateinit var comicViewModel: CurrentComicViewModel
    private lateinit var accountViewModel: MainViewModel
    private lateinit var commentDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReadCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //comicViewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        commentDisplay = binding.readCommentsList

        // set comic title from intent
        binding.editorTitleText.text = "FILL IN COMIC TITLE"

        // fill in list of strings
        if (intent.hasExtra("commentsList")) {
            commentList = intent.getStringArrayListExtra("commentsList")!!
            populateScreen()
        }

        // initial test population of comments
        commentList.add("test1")
        commentList.add("test2")
        commentDisplay.text = commentList.joinToString(separator = "\n")


        // exit the comments page
        binding.buttonSaveAndExit.setOnClickListener {
            finish()
        }

        binding.leaveComment.setOnClickListener {
            val yourComment: String = binding.comment.text.toString()

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
        commentDisplay.text = commentList.joinToString(separator = "\n")
    }

    private fun leaveComment(yourComment: String) {
        // add account name: comment to list and update on firebase
        Log.i("Comment", "yourComment: $yourComment")
        commentList.add(accountViewModel.email.value + ": " + yourComment)
        populateScreen()
    }
}
