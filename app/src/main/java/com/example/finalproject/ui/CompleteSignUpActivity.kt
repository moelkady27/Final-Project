package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_complete_sign_up.btn_next_sign_up
import kotlinx.android.synthetic.main.activity_complete_sign_up.toolbar_complete_sign_up

class CompleteSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_sign_up)

        setUpActionBar()

        btn_next_sign_up.setOnClickListener {
            startActivity(Intent(this@CompleteSignUpActivity, UploadPhotoActivity::class.java))
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_complete_sign_up)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_complete_sign_up.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}