package com.example.finalproject.ui.favourite.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.favourite.adapter.FavouritesAdapter
import kotlinx.android.synthetic.main.activity_favourites.toolbar_favourites

class FavouritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var favouritesAdapter: FavouritesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        setUpActionBar()

        recyclerView = findViewById(R.id.recycle_favourites)

        favouritesAdapter = FavouritesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this@FavouritesActivity , LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = favouritesAdapter
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_favourites)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_favourites.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}