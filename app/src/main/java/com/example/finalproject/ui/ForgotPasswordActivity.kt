package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_forgot_password.btn_send
import kotlinx.android.synthetic.main.activity_forgot_password.toolbar_forget_password

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setUpActionBar()

        btn_send.setOnClickListener {
            startActivity(Intent(this@ForgotPasswordActivity, CheckYourMailActivity::class.java))
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_forget_password)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_forget_password.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}