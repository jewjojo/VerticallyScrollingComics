package com.umdproject.verticallyscrollingcomics.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.umdproject.verticallyscrollingcomics.activities.ReadComic
import com.umdproject.verticallyscrollingcomics.activities.ReadComments
import com.umdproject.verticallyscrollingcomics.dataClasses.ReadableComic
import com.umdproject.verticallyscrollingcomics.dataClasses.ReadableComicList
import com.umdproject.verticallyscrollingcomics.databinding.BrowseFragmentBinding
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel


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

                val comicAdapter = ReadableComicList(requireContext(), readableComics)

                binding.gridView.adapter = comicAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}