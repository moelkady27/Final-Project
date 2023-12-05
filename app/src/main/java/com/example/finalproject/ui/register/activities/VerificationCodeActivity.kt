package com.example.finalproject.ui.register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.ui.ResetPasswordActivity
import kotlinx.android.synthetic.main.activity_verification_code.btn_next_verification_code
import kotlinx.android.synthetic.main.activity_verification_code.toolbar_validation

class VerificationCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)

        btn_next_verification_code.setOnClickListener {
            startActivity(Intent(this@VerificationCodeActivity, ResetPasswordActivity::class.java))
        }

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