package com.example.finalproject.ui.home.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.home.adapter.HomePopularAdapter
import kotlinx.android.synthetic.main.activity_popular_nearest_you.toolbar_popular

class PopularNearestYouActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var homePopularAdapter: HomePopularAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_nearest_you)

        recyclerView = findViewById(R.id.recycle_favourites_popular)

        homePopularAdapter = HomePopularAdapter()

        recyclerView.layoutManager = GridLayoutManager(this@PopularNearestYouActivity , 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = homePopularAdapter

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_popular)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_popular.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}