package com.umdproject.verticallyscrollingcomics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umdproject.verticallyscrollingcomics.ui.main.BrowseFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, BrowseFragment.newInstance())
                .commitNow()
        }
    }
}