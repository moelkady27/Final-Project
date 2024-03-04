package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.ui.password.activities.VerificationCodeForgetPasswordActivity
import com.example.finalproject.ui.register.activities.VerificationCodeSignUpActivity
import kotlinx.android.synthetic.main.activity_check_your_mail.btn_ok
import kotlinx.android.synthetic.main.activity_check_your_mail.toolbar_check

class CheckYourMailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_your_mail)

        setUpActionBar()

        btn_ok.setOnClickListener {
            startActivity(Intent(this@CheckYourMailActivity, VerificationCodeForgetPasswordActivity::class.java))
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_check)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_check.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}