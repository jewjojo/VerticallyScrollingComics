package com.umdproject.verticallyscrollingcomics.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.ktx.storage
import com.umdproject.verticallyscrollingcomics.dataClasses.ReadableComic
import com.umdproject.verticallyscrollingcomics.dataClasses.ReadableComicList
import com.umdproject.verticallyscrollingcomics.databinding.BrowseFragmentBinding
import java.io.File


// This fragment displays the general browsing list for all comics available in app.
// implementation info on scrollable list in recycler view:
// https://developer.android.com/codelabs/basic-android-kotlin-training-affirmations-app#3
// https://developer.android.com/codelabs/basic-android-kotlin-training-display-list-cards/

// create onclick listener for each comic in the list
// https://stackoverflow.com/questions/24471109/recyclerview-onclick
class BrowseFragment : Fragment() {

    companion object {
        fun newInstance() = BrowseFragment()
        const val TAG = "BrowseFragment-FirebaseRealtimeDatabase"
    }

    private lateinit var binding: BrowseFragmentBinding

    private lateinit var readableComics: MutableList<ReadableComic>
    private lateinit var databaseReadableComics: DatabaseReference
    private var storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BrowseFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReadableComics = FirebaseDatabase.getInstance().getReference("readableComics")

        readableComics = ArrayList()
    }

    override fun onStart() {
        super.onStart()

        val comicAdapter = ReadableComicList(requireContext(), readableComics)
        binding.gridView.adapter = comicAdapter

        binding.gridView.setOnItemClickListener() { adapterView, view, position, id ->
            //Log.d("VSC_BROWSE", position.toString())
            var comicId = readableComics[position].comicId
            val downloadDir = File(requireActivity().filesDir, "/downloads/" + comicId)
            if (!downloadDir.exists()) {
                downloadDir.mkdirs()
            }

            var root = storage.reference
            var comicStorageRef = root.child("comics/" + comicId)


            comicStorageRef.listAll()
                .addOnSuccessListener { listResult ->
                    for (item in listResult.items) {
                        comicStorageRef.child(item.name)
                            .getFile(File(requireActivity().filesDir, "/downloads/" + comicId + "/" + item.name))
                    }
                }

        }

        databaseReadableComics.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                readableComics.clear()

                var readableComic: ReadableComic? = null

                for (postSnapshot in dataSnapshot.children) {
                    try {
                        readableComic = postSnapshot.getValue(ReadableComic::class.java)

                    } catch (e: Exception) {
                        Log.e(TAG, e.toString())

                    } finally {
                        readableComics.add(readableComic!!)

                    }
                }
                comicAdapter.comics = readableComics
                comicAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}