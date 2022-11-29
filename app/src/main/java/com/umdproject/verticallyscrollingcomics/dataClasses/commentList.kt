package com.umdproject.verticallyscrollingcomics.dataClasses

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.database.ValueEventListener
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.ReadableComicListItemLayoutBinding
import com.umdproject.verticallyscrollingcomics.databinding.ReadableCommentListItemLayoutBinding
import java.text.FieldPosition

class CommentList(
    context: Context, //ValueEventListener,
    private var comments: List<Comment>,
) : ArrayAdapter<Comment>(
    context,
    R.layout.readable_comment_list_item_layout,
    comments
) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding =
            ReadableCommentListItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        val comment = comments[position]
        binding.commentText.text = comment.commentText
        binding.authorName.text = comment.authorName

        return binding.root
    }


}