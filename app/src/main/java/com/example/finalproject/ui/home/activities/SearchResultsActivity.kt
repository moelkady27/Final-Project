package com.example.finalproject.ui.home.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.favourite.factory.AddToFavouritesFactory
import com.example.finalproject.ui.favourite.factory.DeleteFavouriteFactory
import com.example.finalproject.ui.favourite.repository.AddToFavouritesRepository
import com.example.finalproject.ui.favourite.repository.DeleteFavouriteRepository
import com.example.finalproject.ui.favourite.viewModel.AddToFavouritesViewModel
import com.example.finalproject.ui.favourite.viewModel.DeleteFavouriteViewModel
import com.example.finalproject.ui.home.adapter.SearchResultsAdapter
import com.example.finalproject.ui.home.factory.HomeFeaturedEstatesFactory
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import com.example.finalproject.ui.home.viewModel.HomeFeaturedEstatesViewModel
import kotlinx.android.synthetic.main.activity_search_results.et_search_results
import kotlinx.android.synthetic.main.activity_search_results.toolbar_search_results
import kotlinx.android.synthetic.main.activity_search_results.tv_search_results_title_1
import org.json.JSONException
import org.json.JSONObject

class SearchResultsActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var recyclerView: RecyclerView

    private lateinit var searchResultsAdapter: SearchResultsAdapter

    private lateinit var homeFeaturedEstatesViewModel: HomeFeaturedEstatesViewModel

    private lateinit var addToFavouritesViewModel: AddToFavouritesViewModel

    private lateinit var deleteFavouriteViewModel: DeleteFavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        networkUtils = NetworkUtils(this@SearchResultsActivity)

        initRecyclerView()

        initView()

        initSearch()

        setUpActionBar()
    }

    private fun initView() {
                                    /* Get-All */

        val homeFeaturedEstatesRepository = HomeFeaturedEstatesRepository(RetrofitClient.instance)
        val factory = HomeFeaturedEstatesFactory(homeFeaturedEstatesRepository)
        homeFeaturedEstatesViewModel = ViewModelProvider(
            this@SearchResultsActivity, factory
        )[HomeFeaturedEstatesViewModel::class.java]

        getAllEstates()

                                /* Add-To-Favourite */

        val addToFavouritesRepository = AddToFavouritesRepository(RetrofitClient.instance)
        val addToFavouritesFactory = AddToFavouritesFactory(addToFavouritesRepository)
        addToFavouritesViewModel = ViewModelProvider(
            this@SearchResultsActivity, addToFavouritesFactory
        )[AddToFavouritesViewModel::class.java]

                                /* Delete-Favourite */

        val deleteFavouritesRepository = DeleteFavouriteRepository(RetrofitClient.instance)
        val deleteFavouriteFactory = DeleteFavouriteFactory(deleteFavouritesRepository)
        deleteFavouriteViewModel = ViewModelProvider(
            this@SearchResultsActivity, deleteFavouriteFactory
        )[DeleteFavouriteViewModel::class.java]
    }

    private fun getAllEstates() {
        val token = AppReferences.getToken(this@SearchResultsActivity)

        homeFeaturedEstatesViewModel.getFeaturedEstatesViewAll(token)

        homeFeaturedEstatesViewModel.homeFeaturedEstatesLiveData.observe(this@SearchResultsActivity
        ) { response ->
            hideProgressDialog()
            response?.let {
                searchResultsAdapter.addItems(it.residences)
                Log.e("FeaturedEstates", it.residences.size.toString())
            }
        }

        homeFeaturedEstatesViewModel.errorLiveData.observe(this@SearchResultsActivity) { error ->
            hideProgressDialog()
            error.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@SearchResultsActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
                } catch (_: JSONException) { }
            }
        }

        initPagination(token)
    }

    private fun initPagination(token: String) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 >= totalItemCount) {
                    homeFeaturedEstatesViewModel.getFeaturedEstatesViewAll(token)
                }
            }
        })
    }

    private fun handleFavouriteClick(residenceId: String, isLiked: Boolean) {
        if (!isLiked) {
            val token = AppReferences.getToken(this@SearchResultsActivity)
            addToFavouritesViewModel.addToFavourites(token, residenceId)

            addToFavouritesViewModel.addToFavouritesLiveData.observe(
                this@SearchResultsActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("AddToFavourites", "Added to Favourites $status")
                    searchResultsAdapter.updateFavouriteStatus(residenceId, true)
                }
            }

            addToFavouritesViewModel.errorLiveData.observe(this@SearchResultsActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@SearchResultsActivity, errorMessage, Toast.LENGTH_LONG)
                            .show()
                    } catch (_: JSONException) { }
                }
            }
        } else {
            val token = AppReferences.getToken(this@SearchResultsActivity)
            deleteFavouriteViewModel.deleteFavourite(token, residenceId)

            deleteFavouriteViewModel.deleteFavouriteLiveData.observe(this@SearchResultsActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("DeleteFavourite", "Deleted from Favourites $status")
                    searchResultsAdapter.updateFavouriteStatus(residenceId, false)
                }
            }

            deleteFavouriteViewModel.errorLiveData.observe(this@SearchResultsActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@SearchResultsActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (_: JSONException) { }
                }
            }
        }
    }

    private fun initSearch() {
        et_search_results.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchResultsAdapter.filterList(s.toString())
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycle_search_results)
        recyclerView.layoutManager = LinearLayoutManager(this@SearchResultsActivity ,
            LinearLayoutManager.VERTICAL , false)
        recyclerView.setHasFixedSize(true)
        searchResultsAdapter = SearchResultsAdapter(
            this@SearchResultsActivity, mutableListOf(), ::handleFavouriteClick
        )
        recyclerView.adapter = searchResultsAdapter
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