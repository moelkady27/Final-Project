package com.example.finalproject.ui.booked_residences.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.booked_residences.adapter.AcceptCancelBookedAdapter
import kotlinx.android.synthetic.main.activity_accept_cancel_booked.toolbar_accept_cancel_booked

class AcceptCancelBookedActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var bookedAdapter: AcceptCancelBookedAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_cancel_booked)

        setupActionBar()

        recyclerView = findViewById(R.id.recycle_booked)
        recyclerView.layoutManager = LinearLayoutManager(this@AcceptCancelBookedActivity ,
            LinearLayoutManager.VERTICAL , false)
        bookedAdapter = AcceptCancelBookedAdapter()
        recyclerView.adapter = bookedAdapter

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_accept_cancel_booked)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_accept_cancel_booked.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}