package com.example.finalproject.ui.setting.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_edit_profile.toolbar_edit_profile

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_edit_profile)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_edit_profile.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}