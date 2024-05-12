package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_add_listing_first.btn_next_form_detail
import kotlinx.android.synthetic.main.activity_add_listing_first.toolbar_form_detail

class AddListingFirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_first)

        setUpActionBar()

        btn_next_form_detail.setOnClickListener {
            val intent = Intent(this@AddListingFirstActivity ,
                AddListingLocationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_form_detail)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_form_detail.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun toggleChipSelection(chip: Chip) {
        chip.isSelected = !chip.isSelected
        if (chip.isSelected) {
            chip.setTextColor(Color.WHITE)
            chip.setChipBackgroundColorResource(R.color.colorPrimary)
        } else {
            chip.setTextColor(resources.getColor(R.color.colorPrimaryText))
            chip.setChipBackgroundColorResource(R.color.home_search)
        }
    }

}