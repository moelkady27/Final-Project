package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_reset_password_success.btn_back_reset_password_success

class ResetPasswordSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_success)

        btn_back_reset_password_success.setOnClickListener {
            startActivity(Intent(this@ResetPasswordSuccessActivity, SignInActivity::class.java))
        }

    }
}