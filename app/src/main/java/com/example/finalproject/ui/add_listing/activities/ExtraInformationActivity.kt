package com.example.finalproject.ui.add_listing.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_extra_information.count_balcony
import kotlinx.android.synthetic.main.activity_extra_information.count_bathroom
import kotlinx.android.synthetic.main.activity_extra_information.count_bedroom
import kotlinx.android.synthetic.main.activity_extra_information.minus_balcony
import kotlinx.android.synthetic.main.activity_extra_information.minus_bathroom
import kotlinx.android.synthetic.main.activity_extra_information.minus_bedroom
import kotlinx.android.synthetic.main.activity_extra_information.plus_balcony
import kotlinx.android.synthetic.main.activity_extra_information.plus_bathroom
import kotlinx.android.synthetic.main.activity_extra_information.plus_bedroom
import kotlinx.android.synthetic.main.activity_extra_information.toolbar_extra_information

class ExtraInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_information)

        plus_bedroom.setOnClickListener {
            increaseCountBedroom()
        }

        minus_bedroom.setOnClickListener {
            decreaseCountBedroom()
        }

        plus_bathroom.setOnClickListener {
            increaseCountBathroom()
        }

        minus_bathroom.setOnClickListener {
            decreaseCountBathroom()
        }

        plus_balcony.setOnClickListener {
            increaseCountBalcony()
        }

        minus_balcony.setOnClickListener {
            decreaseCountBalcony()
        }

        setupActionBar()
    }

    private fun increaseCountBedroom() {
        val currentText = count_bedroom.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_bedroom.text = newNumber.toString()
        Log.e("Count Bedroom", newNumber.toString())
    }

    private fun decreaseCountBedroom() {
        val currentText = count_bedroom.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_bedroom.text = newNumber.toString()
            Log.e("Count Bedroom", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountBathroom() {
        val currentText = count_bathroom.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_bathroom.text = newNumber.toString()
        Log.e("Count Bathroom", newNumber.toString())
    }

    private fun decreaseCountBathroom() {
        val currentText = count_bathroom.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_bathroom.text = newNumber.toString()
            Log.e("Count Bathroom", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountBalcony() {
        val currentText = count_balcony.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_balcony.text = newNumber.toString()
        Log.e("Count Balcony", newNumber.toString())
    }

    private fun decreaseCountBalcony() {
        val currentText = count_balcony.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_balcony.text = newNumber.toString()
            Log.e("Count Balcony", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_extra_information)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_extra_information.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}