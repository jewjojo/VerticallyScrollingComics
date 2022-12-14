package com.umdproject.verticallyscrollingcomics.dataClasses

import android.content.Context
import android.graphics.Bitmap
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.ReadableComicListItemLayoutBinding
import java.text.FieldPosition

class ReadableComicList(
    context: Context,
    var comics: List<ReadableComic>,
    var thumbnails: List<Bitmap>
) : ArrayAdapter<ReadableComic>(
    context,
    R.layout.readable_comic_list_item_layout,
    comics
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding =
            ReadableComicListItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        val comic = comics[position]
        binding.name.text = comic.comicName
        binding.author.text = comic.comicAuthorName
        if (position < thumbnails.size) {
            binding.imageView.setImageBitmap(thumbnails[position])
        }

        return binding.root

    }


}