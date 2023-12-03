package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_upload_photo.btn_next_upload_photo
import kotlinx.android.synthetic.main.activity_upload_photo.toolbar_upload_photo
import kotlinx.android.synthetic.main.activity_verification_code.toolbar_validation

class UploadPhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_photo)

        setUpActionBar()

        btn_next_upload_photo.setOnClickListener {
            startActivity(Intent(this@UploadPhotoActivity, UploadPreviewActivity::class.java))
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_upload_photo)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_upload_photo.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}