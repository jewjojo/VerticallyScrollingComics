package com.umdproject.verticallyscrollingcomics.ui.fragments


import com.umdproject.verticallyscrollingcomics.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class ComicAdapter(private val mContext: Context, comics: MutableList<LocalComicPreview>?) :
    BaseAdapter() {
    var comics: MutableList<LocalComicPreview>?

    init {
        this.comics = comics
    }

    override fun getCount(): Int {
        return comics!!.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val comic: LocalComicPreview = comics!![position]

        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            convertView = layoutInflater.inflate(R.layout.comic_preview, null)
        }

        val imageView = convertView!!.findViewById<View>(R.id.imageview_cover_art) as ImageView
        val nameTextView = convertView!!.findViewById<View>(R.id.textview_title) as TextView
        val authorTextView = convertView!!.findViewById<View>(R.id.textview_author) as TextView

        imageView.setImageBitmap(comic.cover)
        nameTextView.text = comic.title

        authorTextView.text = comic.author

        return convertView
    }
}
