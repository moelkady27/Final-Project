package com.example.finalproject.ui.home.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.home.adapter.FeaturedViewAllAdapter
import kotlinx.android.synthetic.main.activity_featured_estates.toolbar_featured_estates

class FeaturedEstatesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var featuredViewAllAdapter: FeaturedViewAllAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_estates)

        recyclerView = findViewById(R.id.recycle_featured_estates)

        featuredViewAllAdapter = FeaturedViewAllAdapter()

        recyclerView.layoutManager = GridLayoutManager(this@FeaturedEstatesActivity , 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = featuredViewAllAdapter

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_featured_estates)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_featured_estates.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}