package com.example.finalproject.ui.residence_details.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R

class ResidenceDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_residence_details)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("ResidenceDetailsActivity", "residenceId: $residenceId")
    }
}