package com.example.finalproject.ui.home.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
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
import com.example.finalproject.ui.home.adapter.FeaturedViewAllAdapter
import com.example.finalproject.ui.home.factory.HomeFeaturedEstatesFactory
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import com.example.finalproject.ui.home.viewModel.HomeFeaturedEstatesViewModel
import kotlinx.android.synthetic.main.activity_featured_estates.et_search_featured_estates
import kotlinx.android.synthetic.main.activity_featured_estates.toolbar_featured_estates
import org.json.JSONException
import org.json.JSONObject

class FeaturedEstatesActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var recyclerView: RecyclerView

    private lateinit var featuredViewAllAdapter: FeaturedViewAllAdapter

    private lateinit var homeFeaturedEstatesViewModel: HomeFeaturedEstatesViewModel

    private lateinit var addToFavouritesViewModel: AddToFavouritesViewModel

    private lateinit var deleteFavouriteViewModel: DeleteFavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_estates)

        networkUtils = NetworkUtils(this@FeaturedEstatesActivity)

        recyclerView = findViewById(R.id.recycle_featured_estates)
        recyclerView.layoutManager = GridLayoutManager(this@FeaturedEstatesActivity , 2)
        recyclerView.setHasFixedSize(true)


        val addToFavouritesRepository = AddToFavouritesRepository(RetrofitClient.instance)
        val addToFavouritesFactory = AddToFavouritesFactory(addToFavouritesRepository)
        addToFavouritesViewModel = ViewModelProvider(
            this@FeaturedEstatesActivity, addToFavouritesFactory
        )[AddToFavouritesViewModel::class.java]

        val deleteFavouritesRepository = DeleteFavouriteRepository(RetrofitClient.instance)
        val deleteFavouriteFactory = DeleteFavouriteFactory(deleteFavouritesRepository)
        deleteFavouriteViewModel = ViewModelProvider(this@FeaturedEstatesActivity, deleteFavouriteFactory
        )[DeleteFavouriteViewModel::class.java]

        featuredViewAllAdapter = FeaturedViewAllAdapter(this@FeaturedEstatesActivity, mutableListOf(), ::handleFavouriteClick)
        recyclerView.adapter = featuredViewAllAdapter

        initView()

        initSearch()

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

        homeFeaturedEstatesViewModel.homeFeaturedEstatesLiveData.observe(this@FeaturedEstatesActivity
        ) { response ->
            hideProgressDialog()
            response?.let {
                featuredViewAllAdapter.addItems(it.residences)
                Log.e("FeaturedEstates", it.residences.size.toString())
            }
        }

        homeFeaturedEstatesViewModel.errorLiveData.observe(this@FeaturedEstatesActivity) { error ->
            hideProgressDialog()
            error.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@FeaturedEstatesActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
                } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }

        initPagination(token)

        val addToFavouritesRepository = AddToFavouritesRepository(RetrofitClient.instance)
        val addToFavouritesFactory = AddToFavouritesFactory(addToFavouritesRepository)
        addToFavouritesViewModel = ViewModelProvider(
            this@FeaturedEstatesActivity, addToFavouritesFactory
        )[AddToFavouritesViewModel::class.java]

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

    private fun handleFavouriteClick(residenceId: String, isLiked: Boolean) {
        if (!isLiked) {
            val token = AppReferences.getToken(this@FeaturedEstatesActivity)
            addToFavouritesViewModel.addToFavourites(token, residenceId)

            addToFavouritesViewModel.addToFavouritesLiveData.observe(this@FeaturedEstatesActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("AddToFavourites", "Added to Favourites $status")
                    featuredViewAllAdapter.updateFavouriteStatus(residenceId, true)
                }
            }

            addToFavouritesViewModel.errorLiveData.observe(this@FeaturedEstatesActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@FeaturedEstatesActivity, errorMessage, Toast.LENGTH_LONG)
                            .show()
                    } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            val token = AppReferences.getToken(this@FeaturedEstatesActivity)
            deleteFavouriteViewModel.deleteFavourite(token, residenceId)

            deleteFavouriteViewModel.deleteFavouriteLiveData.observe(this@FeaturedEstatesActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("DeleteFavourite", "Deleted from Favourites $status")
                    featuredViewAllAdapter.updateFavouriteStatus(residenceId, false)
                }
            }

            deleteFavouriteViewModel.errorLiveData.observe(this@FeaturedEstatesActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@FeaturedEstatesActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun initSearch() {
        et_search_featured_estates.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                featuredViewAllAdapter.filterList(s.toString())
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