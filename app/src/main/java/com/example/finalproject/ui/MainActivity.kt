package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.register.activities.SignInActivity
import kotlinx.android.synthetic.main.activity_main.textView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        logout
        textView.setOnClickListener {
            AppReferences.setLoginState(this@MainActivity, false)
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
            finish()
        }
    }
}