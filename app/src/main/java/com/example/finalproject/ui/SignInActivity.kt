package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.ui.register.activities.SignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.tv_forget_password
import kotlinx.android.synthetic.main.activity_sign_in.tv_register_now

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        tv_register_now.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

        tv_forget_password.setOnClickListener {
            startActivity(Intent(this@SignInActivity , ForgotPasswordActivity::class.java))
        }
    }
}

