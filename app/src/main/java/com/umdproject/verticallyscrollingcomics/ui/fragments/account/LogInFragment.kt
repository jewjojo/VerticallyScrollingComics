package com.umdproject.verticallyscrollingcomics.ui.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.LoginFragmentBinding
import com.umdproject.verticallyscrollingcomics.databinding.LoginSignupFragmentBinding

class LogInFragment : Fragment() {
    companion object {
        fun newInstance() = LogInFragment()
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding = LoginFragmentBinding.inflate(inflater, container, false)

        auth = requireNotNull(FirebaseAuth.getInstance())

        return binding.root

    }
}