package com.example.finalproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_delete_account.toolbar_delete_account
import kotlinx.android.synthetic.main.activity_upload_preview.toolbar_upload_preview

class DeleteAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_delete_account)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_delete_account.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}