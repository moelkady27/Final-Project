package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_upload_photo.btn_next_upload_photo

class UploadPhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_photo)

        btn_next_upload_photo.setOnClickListener {
            startActivity(Intent(this@UploadPhotoActivity, UploadPreviewActivity::class.java))
            finish()
        }

    }
}