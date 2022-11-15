package com.umdproject.verticallyscrollingcomics.ui.fragments.account

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.databinding.LoginFragmentBinding
import com.umdproject.verticallyscrollingcomics.databinding.LoginSignupFragmentBinding
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel

class LogInFragment : Fragment() {
    companion object {
        fun newInstance() = LogInFragment()
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: LoginFragmentBinding
    private lateinit var accountViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = LoginFragmentBinding.inflate(inflater, container, false)
        accountViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        auth = requireNotNull(FirebaseAuth.getInstance())


        binding.login.setOnClickListener { loginUserAccount() }

        binding.fab.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, LoginSignupFragment.newInstance())
                .commitNow()
        }

        return binding.root

    }

    private fun loginUserAccount() {
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.login_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.password_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Login successful!",
                        Toast.LENGTH_LONG
                    ).show()

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, AccountFragment.newInstance())
                        .commitNow()


                    accountViewModel.setEmail(email)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }
}