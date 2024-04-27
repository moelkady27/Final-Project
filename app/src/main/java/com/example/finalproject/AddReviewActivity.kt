package com.example.finalproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_add_review.toolbar_add_review
import kotlinx.android.synthetic.main.activity_form_detail.toolbar_form_detail

class AddReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_add_review)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_add_review.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}