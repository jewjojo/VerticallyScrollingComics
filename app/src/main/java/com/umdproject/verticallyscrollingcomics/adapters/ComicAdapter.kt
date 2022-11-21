package com.umdproject.verticallyscrollingcomics.adapters


import com.umdproject.verticallyscrollingcomics.R
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.umdproject.verticallyscrollingcomics.activities.EditComicActivity
import com.umdproject.verticallyscrollingcomics.ui.fragments.LocalComicPreview


class ComicAdapter(private val activity: FragmentActivity, private val mContext: Context, comics: MutableList<LocalComicPreview>?, uid: Int) :
    BaseAdapter() {
    var comics: MutableList<LocalComicPreview>?
    var uid = uid

    init {
        this.comics = comics
        this.uid = uid
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

    override fun getView(position: Int, convertViewIn: View?, parent: ViewGroup): View {
        var convertView = convertViewIn
        val comic: LocalComicPreview = comics!![position]

        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            convertView = layoutInflater.inflate(R.layout.comic_preview, null)
        }

        val imageView = convertView!!.findViewById<View>(R.id.imageview_cover_art) as ImageView
        val nameTextView = convertView!!.findViewById<View>(R.id.textview_title) as TextView
        val authorTextView = convertView!!.findViewById<View>(R.id.textview_author) as TextView

        var editIntent = Intent(activity, EditComicActivity::class.java)
        editIntent.putExtra("filePath", comic.filePath)
        editIntent.putExtra("uid", uid)
        convertView!!.findViewById<View>(R.id.buttonEdit).setOnClickListener {
            startActivity(mContext, editIntent, null)
        }

        imageView.setImageBitmap(comic.cover)
        nameTextView.text = comic.title

        authorTextView.text = comic.author



        return convertView
    }
}
