package com.umdproject.verticallyscrollingcomics.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umdproject.verticallyscrollingcomics.R

// This fragment displays the list of comics the user has read before
// implementation info on scrollable list in recycler view:
// https://developer.android.com/codelabs/basic-android-kotlin-training-affirmations-app#3
// https://developer.android.com/codelabs/basic-android-kotlin-training-display-list-cards/
class ContinueReadingFragment : Fragment() {

    companion object {
        fun newInstance() = ContinueReadingFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.continue_reading_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}