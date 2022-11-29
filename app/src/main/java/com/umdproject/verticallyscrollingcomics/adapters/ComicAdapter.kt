package com.umdproject.verticallyscrollingcomics.adapters


import com.umdproject.verticallyscrollingcomics.R
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.umdproject.verticallyscrollingcomics.activities.EditComicActivity
import com.umdproject.verticallyscrollingcomics.ui.fragments.LocalComicPreview
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class ComicAdapter(private val activity: FragmentActivity, private val mContext: Context, comics: MutableList<LocalComicPreview>?, uid: Int) :
    BaseAdapter() {
    var comics: MutableList<LocalComicPreview>?
    var uid = uid

    private var storage = Firebase.storage
    private val database = Firebase.database
    private var auth: FirebaseAuth

    init {
        this.comics = comics
        this.uid = uid
        this.auth = FirebaseAuth.getInstance()
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

        convertView!!.findViewById<View>(R.id.buttonPublish).setOnClickListener {
            if (auth.currentUser != null) {
                val comDir = File(comic.filePath)
                var comicId = comic.filePath.takeLast(9)
                var root = storage.reference
                var comicStorageRef = root.child("comics/" + comicId)

                var failed = false

                comDir.walk().forEach {
                    if (it.extension == "png" || it.extension == "json") {
                        val currRef = comicStorageRef.child(it.name)
                        val uploadTask = currRef.putFile(Uri.fromFile(it))
                        uploadTask.addOnFailureListener {
                            failed = true
                        }.addOnSuccessListener { taskSnapshot ->
                            //....
                        }
                    }
                }
                if (!failed) {
                    val idRef = database.getReference("readableComics/" + comicId + "/" + "id")
                    idRef.setValue(comicId)
                    val infoObject = ComicInfo()
                    infoObject.readData(comic.filePath)
                    val comicNameRef = database.getReference("readableComics/" + comicId + "/" + "comicName")
                    comicNameRef.setValue(infoObject.title)
                    val comicAuthorRef = database.getReference("readableComics/" + comicId + "/" + "comicAuthorName")
                    comicAuthorRef.setValue(infoObject.author)
                    val comicAuthorIdRef = database.getReference("readableComics/" + comicId + "/" + "comicAuthorId")
                    comicAuthorIdRef.setValue(auth.uid)
                    val comicIdRef = database.getReference("readableComics/" + comicId + "/" + "comicId")
                    comicIdRef.setValue(comicId)
                    val panelCountRef = database.getReference("readableComics/" + comicId + "/" + "panelCount")
                    panelCountRef.setValue(0)
                    val thumbnailResIDRef = database.getReference("readableComics/" + comicId + "/" + "thumbnailResId")
                    thumbnailResIDRef.setValue("")
                    val viewsRef = database.getReference("readableComics/" + comicId + "/" + "views")
                    viewsRef.setValue(0)
                    val ratingCountRef = database.getReference("readableComics/" + comicId + "/" + "ratingCount")
                    ratingCountRef.setValue(0)
                    val ratingAverageRef = database.getReference("readableComics/" + comicId + "/" + "ratingAverage")
                    ratingAverageRef.setValue(0)

                    Toast.makeText(mContext, "Successfully uploaded comic!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(mContext, "Failed to upload comic", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(mContext, "You need to sign in to publish comics!", Toast.LENGTH_SHORT).show()
            }


        }

        imageView.setImageBitmap(comic.cover)
        nameTextView.text = comic.title

        authorTextView.text = comic.author



        return convertView
    }

    class ComicInfo {
        var author: String
        var title: String
        init {
            author = ""
            title = ""
        }

        fun readData(filePath: String) {
            val fileInputStream = FileInputStream(filePath + "/metadata.json")
            val jsonReader = JsonReader(InputStreamReader(fileInputStream, "UTF-8"))
            jsonReader.beginObject()

            while (jsonReader.hasNext()) {
                val tokenName = jsonReader.nextName()
                if (tokenName.equals("title")) {
                    title = jsonReader.nextString()
                } else if (tokenName.equals("author")) {
                    author = jsonReader.nextString()

                } else {
                    jsonReader.skipValue()
                }
            }
            jsonReader.endObject()
        }
    }
}
