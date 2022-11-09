package com.umdproject.verticallyscrollingcomics.ui.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umdproject.verticallyscrollingcomics.databinding.RegistrationFragmentBinding

class RegisterFragment : Fragment() {
    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = RegistrationFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}