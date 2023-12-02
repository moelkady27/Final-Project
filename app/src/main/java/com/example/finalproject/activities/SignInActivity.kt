package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_sign_in.btn_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.tv_register_now

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        tv_register_now.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            finish()
        }

    }
}