package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.ui.register.activities.SignInActivity
import kotlinx.android.synthetic.main.activity_congrats.btn_start_sign_up_success

class CongratsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrats)

        btn_start_sign_up_success.setOnClickListener {
            startActivity(Intent(this@CongratsActivity, SignInActivity::class.java))
        }

    }
}