package com.example.finalproject.ui.residence_details.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.finalproject.R
import com.example.finalproject.ui.residence_details.adapter.DetailsViewAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_residence_details.tabLayout_residence_details

class ResidenceDetailsActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: DetailsViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_residence_details)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("ResidenceDetailsActivity", "residenceId: $residenceId")

        viewPager2 = findViewById(R.id.view_pager_residence_details)

        adapter = DetailsViewAdapter(supportFragmentManager, lifecycle, residenceId!!)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout_residence_details, viewPager2) { tab, position ->
            when (position) {
                0 ->
                    tab.text = "Description"
                1 ->
                    tab.text = "Gallery"
                2 ->
                    tab.text = "Review"
            }
        }.attach()
    }
}