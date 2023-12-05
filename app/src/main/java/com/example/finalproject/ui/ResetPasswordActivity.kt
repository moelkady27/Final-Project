package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_reset_password.btn_next_reset_password
import kotlinx.android.synthetic.main.activity_reset_password.toolbar_reset_password

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btn_next_reset_password.setOnClickListener {
            startActivity(Intent(this@ResetPasswordActivity, ResetPasswordSuccessActivity::class.java))
        }

        setUpActionBar()

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_reset_password)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_reset_password.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}