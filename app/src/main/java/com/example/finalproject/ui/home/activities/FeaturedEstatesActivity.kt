package com.example.finalproject.ui.home.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.home.adapter.FeaturedViewAllAdapter
import com.example.finalproject.ui.home.factory.HomeFeaturedEstatesFactory
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import com.example.finalproject.ui.home.viewModel.HomeFeaturedEstatesViewModel
import kotlinx.android.synthetic.main.activity_featured_estates.toolbar_featured_estates

class FeaturedEstatesActivity : BaseActivity() {
    private lateinit var networkUtils: NetworkUtils

    private lateinit var recyclerView: RecyclerView

    private lateinit var featuredViewAllAdapter: FeaturedViewAllAdapter

    private lateinit var homeFeaturedEstatesViewModel: HomeFeaturedEstatesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_estates)

        networkUtils = NetworkUtils(this@FeaturedEstatesActivity)

        recyclerView = findViewById(R.id.recycle_featured_estates)

        recyclerView.layoutManager = GridLayoutManager(this@FeaturedEstatesActivity , 2)
        recyclerView.setHasFixedSize(true)
        featuredViewAllAdapter = FeaturedViewAllAdapter(mutableListOf())
        recyclerView.adapter = featuredViewAllAdapter

        initView()

        setUpActionBar()
    }

    private fun initView() {
        val homeFeaturedEstatesRepository = HomeFeaturedEstatesRepository(RetrofitClient.instance)
        val factory = HomeFeaturedEstatesFactory(homeFeaturedEstatesRepository)
        homeFeaturedEstatesViewModel = ViewModelProvider(
            this@FeaturedEstatesActivity, factory
        )[HomeFeaturedEstatesViewModel::class.java]

        val token = AppReferences.getToken(this@FeaturedEstatesActivity)

        homeFeaturedEstatesViewModel.getFeaturedEstatesViewAll(token)

        homeFeaturedEstatesViewModel.homeFeaturedEstatesLiveData.observe(
            this@FeaturedEstatesActivity
        ) { response ->
            hideProgressDialog()
            response?.let {
                featuredViewAllAdapter.addItems(it.residences)
                Log.e("FeaturedEstates", it.residences.size.toString())
            }
        }
        initPagination(token)
    }

    private fun initPagination(token: String) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 >= totalItemCount) {
                    homeFeaturedEstatesViewModel.getFeaturedEstatesViewAll(token)
                }
            }
        })
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