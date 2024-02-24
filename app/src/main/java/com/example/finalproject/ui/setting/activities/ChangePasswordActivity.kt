package com.example.finalproject.ui.setting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_change_password.toolbar_change_password
import kotlinx.android.synthetic.main.activity_delete_account.toolbar_delete_account
import kotlinx.android.synthetic.main.activity_forgot_password.toolbar_forget_password

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_change_password)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_change_password.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}