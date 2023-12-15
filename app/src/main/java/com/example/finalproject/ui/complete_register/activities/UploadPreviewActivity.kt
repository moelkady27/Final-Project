package com.example.finalproject.ui.complete_register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.register.activities.SignInActivity
import kotlinx.android.synthetic.main.activity_upload_preview.btn_next_upload_preview
import kotlinx.android.synthetic.main.activity_upload_preview.ib_upload_preview
import kotlinx.android.synthetic.main.activity_upload_preview.toolbar_upload_preview

class UploadPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_preview)

        val imageUrl = intent.getStringExtra("imageUri")

        Glide
            .with(this)
            .load(imageUrl)
            .centerCrop()
            .into(ib_upload_preview)

        btn_next_upload_preview.setOnClickListener {
            startActivity(Intent(this@UploadPreviewActivity , SignInActivity::class.java))
        }

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