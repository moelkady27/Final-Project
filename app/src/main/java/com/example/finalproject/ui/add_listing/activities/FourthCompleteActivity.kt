package com.example.finalproject.ui.add_listing.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_fourth_complete.toolbar_fourth_complete

class FourthCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth_complete)

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_fourth_complete)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_fourth_complete.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}