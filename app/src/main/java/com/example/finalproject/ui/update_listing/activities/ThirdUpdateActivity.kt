package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_second_update.toolbar_second_update
import kotlinx.android.synthetic.main.activity_third_update.btn_next_third_update
import kotlinx.android.synthetic.main.activity_third_update.toolbar_third_update

class ThirdUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_update)

        btn_next_third_update.setOnClickListener {
            val intent = Intent(
                this@ThirdUpdateActivity, FourthUpdateActivity::class.java)
            startActivity(intent)
        }

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_third_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_third_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}