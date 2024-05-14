package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_add_listing_location.select_on_the_map
import kotlinx.android.synthetic.main.activity_add_listing_location.toolbar_add_listing_location

class AddListingLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_location)

        setUpActionBar()

        val id = intent.getStringExtra("residenceId").toString()
        Log.e("residenceId" , "location is $id")

        select_on_the_map.setOnClickListener {
            val intent = Intent(this@AddListingLocationActivity,
                FirstCompleteActivity::class.java)
            intent.putExtra("residenceId", id)
            startActivity(intent)
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_add_listing_location)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_add_listing_location.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}