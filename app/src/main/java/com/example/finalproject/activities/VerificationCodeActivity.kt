package com.example.finalproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_verification_code.toolbar_validation

class VerificationCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)

        setUpActionBar()

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_validation)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_validation.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}