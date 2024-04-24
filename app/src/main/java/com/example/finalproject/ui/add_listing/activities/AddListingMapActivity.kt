package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_add_listing_map.btn_add_listing_confirm_location

class AddListingMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_map)

        btn_add_listing_confirm_location.setOnClickListener {
            startActivity(Intent(this, ExtraInformationActivity::class.java))
        }
    }
}