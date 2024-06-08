package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.update_listing.adapter.UpdateListingPhotosAdapter
import kotlinx.android.synthetic.main.activity_update_residence.btn_next_update_listing
import kotlinx.android.synthetic.main.activity_update_residence.toolbar_update_residence

class UpdateResidenceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: UpdateListingPhotosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_residence)

        recyclerView = findViewById(R.id.recyclerView_update_listing_photos)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = UpdateListingPhotosAdapter()
        recyclerView.adapter = adapter

        btn_next_update_listing.setOnClickListener {
            val intent = Intent(
                this@UpdateResidenceActivity, FirstUpdateActivity::class.java)
            startActivity(intent)
        }

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_update_residence)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_update_residence.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}