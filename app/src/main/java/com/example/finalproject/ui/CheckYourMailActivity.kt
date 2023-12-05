package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_check_your_mail.btn_ok

class CheckYourMailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_your_mail)

        btn_ok.setOnClickListener {
            startActivity(Intent(this@CheckYourMailActivity, VerificationCodeActivity::class.java))
        }

    }
}