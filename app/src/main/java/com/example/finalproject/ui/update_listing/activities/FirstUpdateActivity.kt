package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_first_update.btn_next_first_update
import kotlinx.android.synthetic.main.activity_first_update.toolbar_first_update

class FirstUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_update)

        btn_next_first_update.setOnClickListener {
            val intent = Intent(
                this@FirstUpdateActivity, SecondUpdateActivity::class.java)
            startActivity(intent)
        }

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_first_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_first_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}