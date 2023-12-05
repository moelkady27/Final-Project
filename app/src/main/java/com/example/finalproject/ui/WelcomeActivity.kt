package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.register.activities.SignInActivity
import com.example.finalproject.ui.register.activities.SignUpActivity
import kotlinx.android.synthetic.main.activity_welcome.btn_sign_in_welcome_screen
import kotlinx.android.synthetic.main.activity_welcome.btn_sign_up_welcome_screen

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_sign_up_welcome_screen.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, SignUpActivity::class.java))
        }

        btn_sign_in_welcome_screen.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, SignInActivity::class.java))
        }

        //                    ..................auto login................
        if (AppReferences.getLoginState(this@WelcomeActivity)){
            startActivity(Intent(this@WelcomeActivity , MainActivity::class.java))
            finish()
        }

    }
}