package com.umdproject.verticallyscrollingcomics.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel

// This fragment displays the comic currently being read, in coordination with it's ViewModel
// The viewModel stores the pages of the comic currently being read, allowing the user to return later
class CurrentComicFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentComicFragment()
    }

    private lateinit var viewModel: CurrentComicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_comic_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentComicViewModel::class.java)
        // TODO: Use the ViewModel
    }

}