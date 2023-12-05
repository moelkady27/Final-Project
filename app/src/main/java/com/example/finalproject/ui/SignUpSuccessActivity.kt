package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_sign_up_success.btn_start_sign_up_success

class SignUpSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_success)

        btn_start_sign_up_success.setOnClickListener {
            startActivity(Intent(this@SignUpSuccessActivity, SignInActivity::class.java))
        }

    }
}