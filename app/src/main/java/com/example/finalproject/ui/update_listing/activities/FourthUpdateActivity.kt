package com.example.finalproject.ui.update_listing.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_fourth_update.toolbar_fourth_update

class FourthUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth_update)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_fourth_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_fourth_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}