package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_complete_sign_up.btn_next_sign_up

class CompleteSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_sign_up)

        btn_next_sign_up.setOnClickListener {
            startActivity(Intent(this@CompleteSignUpActivity, UploadPhotoActivity::class.java))
            finish()
        }

    }
}