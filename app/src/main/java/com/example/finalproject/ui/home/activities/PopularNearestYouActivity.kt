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
import com.example.finalproject.ui.home.adapter.PopularViewAllAdapter
import com.example.finalproject.ui.home.factory.HomePopularEstatesFactory
import com.example.finalproject.ui.home.repository.HomePopularEstatesRepository
import com.example.finalproject.ui.home.viewModel.HomePopularEstatesViewModel
import kotlinx.android.synthetic.main.activity_popular_nearest_you.et_search_popular
import kotlinx.android.synthetic.main.activity_popular_nearest_you.toolbar_popular
import org.json.JSONException
import org.json.JSONObject

class PopularNearestYouActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var recyclerView: RecyclerView

    private lateinit var popularViewAllAdapter: PopularViewAllAdapter

    private lateinit var homePopularEstatesViewModel: HomePopularEstatesViewModel

    private lateinit var addToFavouritesViewModel: AddToFavouritesViewModel

    private lateinit var deleteFavouriteViewModel: DeleteFavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_nearest_you)

        networkUtils = NetworkUtils(this@PopularNearestYouActivity)

        initRecyclerView()

        initSearch()

        initView()

        setUpActionBar()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycle_favourites_popular)
        recyclerView.layoutManager = GridLayoutManager(
            this@PopularNearestYouActivity , 2)
        recyclerView.setHasFixedSize(true)

        popularViewAllAdapter = PopularViewAllAdapter(mutableListOf(), ::handleFavouriteClick)
        recyclerView.adapter = popularViewAllAdapter
    }

    private fun initView() {

                                    /* Popular Estates View All */

        val homePopularEstatesRepository = HomePopularEstatesRepository(RetrofitClient.instance)
        val factory = HomePopularEstatesFactory(homePopularEstatesRepository)
        homePopularEstatesViewModel = ViewModelProvider(
            this@PopularNearestYouActivity, factory
        )[HomePopularEstatesViewModel::class.java]

        val token = AppReferences.getToken(this@PopularNearestYouActivity)

        homePopularEstatesViewModel.getPopularEstatesViewAll(token)

        homePopularEstatesViewModel.homePopularEstatesLiveData.observe(this@PopularNearestYouActivity
        ) { response ->
            hideProgressDialog()
            response?.let {
                popularViewAllAdapter.addItems(it.residences)
                Log.e("PopularEstates", it.residences.size.toString())
            }
        }

        homePopularEstatesViewModel.errorLiveData.observe(this@PopularNearestYouActivity) { error ->
            hideProgressDialog()
            error.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        this@PopularNearestYouActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: JSONException) {
                  Toast.makeText(
                      this@PopularNearestYouActivity,
                      error,
                      Toast.LENGTH_LONG
                  ).show()
                }
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
                    homePopularEstatesViewModel.getPopularEstatesViewAll(token)
                }
            }
        })
    }

    private fun handleFavouriteClick(residenceId: String, isLiked: Boolean) {
        if (!isLiked) {

                                    /* Add To Favourites */

            val addToFavouritesRepository = AddToFavouritesRepository(RetrofitClient.instance)
            val addToFavouritesFactory = AddToFavouritesFactory(addToFavouritesRepository)
            addToFavouritesViewModel = ViewModelProvider(
                this@PopularNearestYouActivity, addToFavouritesFactory
            )[AddToFavouritesViewModel::class.java]

            val token = AppReferences.getToken(this@PopularNearestYouActivity)

            addToFavouritesViewModel.addToFavourites(token, residenceId)

            addToFavouritesViewModel.addToFavouritesLiveData.removeObservers(this)
            addToFavouritesViewModel.addToFavouritesLiveData.observe(
                this@PopularNearestYouActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("AddToFavourites", "Added to Favourites $status")
                    popularViewAllAdapter.updateFavouriteStatus(residenceId, true)
                }
            }

            addToFavouritesViewModel.errorLiveData.removeObservers(this)
            addToFavouritesViewModel.errorLiveData.observe(this@PopularNearestYouActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@PopularNearestYouActivity,
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.e("AddToFavourites", errorMessage)
                    } catch (e: JSONException) {
                      Toast.makeText(
                          this@PopularNearestYouActivity,
                          error,
                          Toast.LENGTH_LONG
                      ).show()

                        Log.e("AddToFavourites error is", error)
                    }
                }
            }
        } else {

                                    /* Delete Favourite */

            val deleteFavouritesRepository = DeleteFavouriteRepository(RetrofitClient.instance)
            val deleteFavouriteFactory = DeleteFavouriteFactory(deleteFavouritesRepository)
            deleteFavouriteViewModel = ViewModelProvider(
                this@PopularNearestYouActivity, deleteFavouriteFactory
            )[DeleteFavouriteViewModel::class.java]

            val token = AppReferences.getToken(this@PopularNearestYouActivity)
            deleteFavouriteViewModel.deleteFavourite(token, residenceId)

            deleteFavouriteViewModel.deleteFavouriteLiveData.removeObservers(this)
            deleteFavouriteViewModel.deleteFavouriteLiveData.observe(
                this@PopularNearestYouActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("DeleteFavourite", "Deleted from Favourites $status")
                    popularViewAllAdapter.updateFavouriteStatus(residenceId, false)
                }
            }

            deleteFavouriteViewModel.errorLiveData.removeObservers(this)
            deleteFavouriteViewModel.errorLiveData.observe(this@PopularNearestYouActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@PopularNearestYouActivity,
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: JSONException) {
                      Toast.makeText(
                          this@PopularNearestYouActivity,
                          error,
                          Toast.LENGTH_LONG
                      ).show()
                    }
                }
            }
        }
    }

    private fun initSearch() {
        et_search_popular.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                popularViewAllAdapter.filterList(s.toString())
            }
        })
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