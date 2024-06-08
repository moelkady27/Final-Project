package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_second_update.btn_next_second_update
import kotlinx.android.synthetic.main.activity_second_update.toolbar_second_update

class SecondUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_update)

        btn_next_second_update.setOnClickListener {
            val intent = Intent(
                this@SecondUpdateActivity, ThirdUpdateActivity::class.java)
            startActivity(intent)
        }

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_second_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_second_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}