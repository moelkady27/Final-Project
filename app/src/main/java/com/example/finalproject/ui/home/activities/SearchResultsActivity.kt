package com.example.finalproject.ui.home.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.home.adapter.SearchResultsAdapter
import kotlinx.android.synthetic.main.activity_search_results.toolbar_search_results

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var searchResultsAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        recyclerView = findViewById(R.id.recycle_search_results)

        searchResultsAdapter = SearchResultsAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this@SearchResultsActivity ,
            LinearLayoutManager.VERTICAL , false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = searchResultsAdapter

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_search_results)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_search_results.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}