package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_sign_up.btn_next_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.tv_login

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        tv_login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }

        btn_next_sign_up.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, CompleteSignUpActivity::class.java))
            finish()
        }

    }
}