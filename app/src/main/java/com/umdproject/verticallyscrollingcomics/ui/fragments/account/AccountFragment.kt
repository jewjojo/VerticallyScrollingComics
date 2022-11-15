package com.umdproject.verticallyscrollingcomics.ui.fragments.account

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.umdproject.verticallyscrollingcomics.viewModels.MainViewModel
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.activities.AccountActivity
import com.umdproject.verticallyscrollingcomics.activities.MainActivity
import com.umdproject.verticallyscrollingcomics.databinding.AccountActivityBinding
import com.umdproject.verticallyscrollingcomics.databinding.AccountFragmentBinding
import com.umdproject.verticallyscrollingcomics.databinding.LoginFragmentBinding
import com.umdproject.verticallyscrollingcomics.ui.fragments.BrowseFragment

// This fragment handles and displays the login page and user auth to firebase.
class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = AccountFragmentBinding.inflate(inflater, container, false)

        auth = requireNotNull(FirebaseAuth.getInstance())
        user = auth.currentUser!!

        username = user.email!!
        var text = getString(R.string.logged_in_as, username)
        binding.titleText.text = text

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(
                requireContext(),
                "You are now logged out!",
                Toast.LENGTH_SHORT
            ).show()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, LoginSignupFragment.newInstance())
                .commitNow()
        }

        binding.fab.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}