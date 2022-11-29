package com.umdproject.verticallyscrollingcomics.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.umdproject.verticallyscrollingcomics.activities.EditComicActivity
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.adapters.ComicAdapter


// This fragment displays the create page for the user, and allows saving to firebase for created comics
class CreateFragment : Fragment() {

    companion object {
        fun newInstance() = CreateFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mContext : Context
    //private var storage = Firebase.storage
    val testArr = arrayListOf(
        LocalComicPreview(Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), "dummy", "Author 1", "Title 1"),
        LocalComicPreview(Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565), "dummy", "Author 2", "Title 2")
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.create_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val gridView = view.findViewById(R.id.gridview) as GridView
        val comicAdapter = ComicAdapter(requireActivity(), mContext, viewModel.comicPreviews.value, viewModel.uid.value!!)
        gridView.adapter = comicAdapter

        viewModel.comicPreviews.observe(viewLifecycleOwner) {
            Log.d("VSC_CF", "CF observed comicPreviews update")
            comicAdapter.comics = it
            comicAdapter.notifyDataSetChanged()
        }

        val createButton = view.findViewById(R.id.fab) as FloatingActionButton
        createButton.setOnClickListener {
            val createFromScratchIntent = Intent(activity, EditComicActivity::class.java)
            createFromScratchIntent.putExtra("uid", viewModel.uid.value)
            startActivity(createFromScratchIntent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}