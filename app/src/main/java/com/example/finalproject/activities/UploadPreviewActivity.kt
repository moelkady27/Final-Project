package com.example.finalproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_upload_preview.toolbar_upload_preview
import kotlinx.android.synthetic.main.activity_verification_code.toolbar_validation

class UploadPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_preview)

        setUpActionBar()

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_upload_preview)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_upload_preview.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}