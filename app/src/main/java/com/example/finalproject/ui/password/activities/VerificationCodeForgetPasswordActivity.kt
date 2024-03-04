package com.example.finalproject.ui.password.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_verification_code_forget_password.toolbar_validation_forget
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.toolbar_validation

class VerificationCodeForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code_forget_password)

        setUpActionBar()

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_validation_forget)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_validation_forget.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}