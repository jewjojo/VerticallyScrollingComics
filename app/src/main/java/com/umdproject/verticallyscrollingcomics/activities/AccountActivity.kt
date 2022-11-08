package com.umdproject.verticallyscrollingcomics.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.umdproject.verticallyscrollingcomics.databinding.AccountActivityBinding
import com.umdproject.verticallyscrollingcomics.ui.fragments.LoginSignupFragment

class AccountActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AccountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, LoginSignupFragment.newInstance())
                .commitNow()
        }


    }
}