package com.umdproject.verticallyscrollingcomics.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.BrowseFragmentBinding
import com.umdproject.verticallyscrollingcomics.dataClasses.ReadableComic
import com.umdproject.verticallyscrollingcomics.dataClasses.ReadableComicList

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

    private lateinit var viewModel: MainViewModel
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

                binding.listViewComics.adapter = comicAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}