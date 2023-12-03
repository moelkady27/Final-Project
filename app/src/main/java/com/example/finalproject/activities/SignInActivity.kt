package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.example.finalproject.R
import com.google.android.material.textfield.TextInputLayout
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

