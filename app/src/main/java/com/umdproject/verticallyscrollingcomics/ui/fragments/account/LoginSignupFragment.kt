package com.umdproject.verticallyscrollingcomics.ui.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.LoginSignupFragmentBinding

class LoginSignupFragment : Fragment() {
    companion object {
        fun newInstance() = LoginSignupFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val auth = requireNotNull(FirebaseAuth.getInstance())
        val user = auth.currentUser

        val binding = LoginSignupFragmentBinding.inflate(inflater, container, false)

        binding.login.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, LogInFragment.newInstance())
                .commitNow()
        }

        binding.register.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, RegisterFragment.newInstance())
                .commitNow()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }
}