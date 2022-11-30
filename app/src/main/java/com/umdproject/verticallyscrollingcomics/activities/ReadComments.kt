package com.umdproject.verticallyscrollingcomics.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.dataClasses.Comment
import com.umdproject.verticallyscrollingcomics.dataClasses.CommentList
import com.umdproject.verticallyscrollingcomics.databinding.ReadCommentsBinding
import com.umdproject.verticallyscrollingcomics.ui.fragments.BrowseFragment
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel


// check GraphicsPaint in class repo to paint toolbar
class ReadComments : AppCompatActivity() {
    private lateinit var comicViewModel: CurrentComicViewModel
    private lateinit var accountViewModel: MainViewModel
    private lateinit var comments: MutableList<Comment>

    private lateinit var auth: FirebaseAuth


    private lateinit var databaseReadableComments: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ReadCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        comicViewModel = ViewModelProvider(this)[CurrentComicViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        auth = FirebaseAuth.getInstance()

        // set comic title from intent
        binding.editorTitleText.text = comicViewModel.title.toString()

        // exit the comments page
        binding.buttonSaveAndExit.setOnClickListener {
            finish()
        }

        binding.leaveComment.setOnClickListener {
            val yourComment: String = binding.comment.text.toString()

            //check if signed in
            if (auth.currentUser == null || auth.currentUser!!.uid == "0") {
                Toast.makeText(
                    this,
                    getString(R.string.sign_in_toast),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (!TextUtils.isEmpty(yourComment)) {
                leaveComment(yourComment)
                binding.leaveComment.text = ""
            }

        }

        // firebase event listeners
        val comicID = intent.getStringExtra("comicID")!!
        databaseReadableComments =
            FirebaseDatabase.getInstance().getReference("comicComments/$comicID")
        comments = ArrayList()

        databaseReadableComments.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                comments.clear()

                var singleComment: Comment? = null
                for (postSnapshot in dataSnapshot.children) {
                    try {
                        singleComment = postSnapshot.getValue(Comment::class.java)

                    } catch (e: Exception) {
                        Log.e(BrowseFragment.TAG, e.toString())

                    } finally {
                        comments.add(singleComment!!)

                    }
                }

                // fill in list of strings
                //populateScreen()
                val commentAdapter = CommentList(this@ReadComments, comments)
                binding.readCommentsList.adapter = commentAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    /*private fun populateScreen() {
        var tempList = comicViewModel.commentsList.value
        commentDisplay.text = tempList?.joinToString(separator = "\n")
        commentDisplay.movementMethod = ScrollingMovementMethod()
    }*/

    private fun leaveComment(yourComment: String) {
        // Getting the values to save.

        val id = databaseReadableComments.push().key

        // create comment object
        val comment = Comment(id!!, auth.currentUser!!.email!!, yourComment)

        // add comment to firebase
        databaseReadableComments.child(id).setValue(comment)
    }
}
