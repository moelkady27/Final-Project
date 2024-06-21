package com.example.finalproject.ui.favourite.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.finalproject.ui.recommendation.models.Data
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.favourite.adapter.FavouritesAdapter
import com.example.finalproject.ui.favourite.adapter.RecommendedPropertiesAdapter
import com.example.finalproject.ui.favourite.factory.AddToFavouritesFactory
import com.example.finalproject.ui.favourite.factory.AllFavouritesFactory
import com.example.finalproject.ui.favourite.factory.DeleteAllFavouriteFactory
import com.example.finalproject.ui.favourite.factory.DeleteFavouriteFactory
import com.example.finalproject.ui.favourite.models.Wishlist
import com.example.finalproject.ui.favourite.repository.AddToFavouritesRepository
import com.example.finalproject.ui.favourite.repository.AllFavouritesRepository
import com.example.finalproject.ui.favourite.repository.DeleteAllFavouriteRepository
import com.example.finalproject.ui.favourite.repository.DeleteFavouriteRepository
import com.example.finalproject.ui.favourite.viewModel.AddToFavouritesViewModel
import com.example.finalproject.ui.favourite.viewModel.AllFavouritesViewModel
import com.example.finalproject.ui.favourite.viewModel.DeleteAllFavouriteViewModel
import com.example.finalproject.ui.favourite.viewModel.DeleteFavouriteViewModel
import com.example.finalproject.ui.recommendation.factory.RecommendationFactory
import com.example.finalproject.ui.recommendation.repository.RecommendationRepository
import com.example.finalproject.ui.recommendation.viewModel.RecommendationViewModel
import kotlinx.android.synthetic.main.activity_favourites.iv_delete_favourites
import kotlinx.android.synthetic.main.activity_favourites.iv_empty_favourites
import kotlinx.android.synthetic.main.activity_favourites.number_favourites
import kotlinx.android.synthetic.main.activity_favourites.recycle_favourites
import kotlinx.android.synthetic.main.activity_favourites.rv_recommended_properties_you_may_like
import kotlinx.android.synthetic.main.activity_favourites.toolbar_favourites
import kotlinx.android.synthetic.main.activity_favourites.tv_empty_favourites_title_2
import kotlinx.android.synthetic.main.activity_favourites.tv_empty_favourites_title_3
import kotlinx.android.synthetic.main.activity_favourites.tv_empty_favourites_title_4
import kotlinx.android.synthetic.main.activity_favourites.tv_recommended_properties_fav
import org.json.JSONException
import org.json.JSONObject

class FavouritesActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var favouritesAdapter: FavouritesAdapter

    private lateinit var recommendedPropertiesAdapter: RecommendedPropertiesAdapter

    private lateinit var allFavouritesViewModel: AllFavouritesViewModel

    private lateinit var deleteAllFavouriteViewModel: DeleteAllFavouriteViewModel

    private lateinit var deleteFavouriteViewModel: DeleteFavouriteViewModel

    private lateinit var networkUtils: NetworkUtils

    private lateinit var recommendationViewModel: RecommendationViewModel

    private lateinit var addToFavouritesViewModel: AddToFavouritesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        recyclerView = findViewById(R.id.rv_recommended_properties_you_may_like)
        recyclerView.layoutManager = LinearLayoutManager(this@FavouritesActivity ,
            LinearLayoutManager.HORIZONTAL , false)
        recommendedPropertiesAdapter = RecommendedPropertiesAdapter(emptyList(),::handleFavouriteClick)
        recyclerView.adapter = recommendedPropertiesAdapter

        networkUtils = NetworkUtils(this@FavouritesActivity)

        initView()

        setUpActionBar()
    }

    private fun initView() {

        val addToFavouritesRepository = AddToFavouritesRepository(RetrofitClient.instance)
        val addToFavouritesFactory = AddToFavouritesFactory(addToFavouritesRepository)
        addToFavouritesViewModel = ViewModelProvider(
            this@FavouritesActivity, addToFavouritesFactory
        )[AddToFavouritesViewModel::class.java]
                                    /* Get-Favourites */

        val allFavouritesRepository = AllFavouritesRepository(RetrofitClient.instance)
        val factory = AllFavouritesFactory(allFavouritesRepository)
        allFavouritesViewModel = ViewModelProvider(this@FavouritesActivity, factory
        )[AllFavouritesViewModel::class.java]

        recyclerView = findViewById(R.id.recycle_favourites)
        recyclerView.layoutManager = LinearLayoutManager(this@FavouritesActivity ,
            LinearLayoutManager.VERTICAL , false)

        if (networkUtils.isNetworkAvailable()) {
            showProgressDialog(this@FavouritesActivity, "please wait...")
            getFavourites()
        } else {
            showErrorSnackBar("No internet connection", true)
        }

                                    /* Delete-Favourite */

        val deleteFavouriteRepository = DeleteFavouriteRepository(RetrofitClient.instance)
        val deleteFavouriteFactory = DeleteFavouriteFactory(deleteFavouriteRepository)
        deleteFavouriteViewModel = ViewModelProvider(this@FavouritesActivity, deleteFavouriteFactory
        )[DeleteFavouriteViewModel::class.java]

                                /* Get-Recommend */

        val recommendationRepository = RecommendationRepository(RetrofitClient.instance)
        val recommendationFactory = RecommendationFactory(recommendationRepository)
        recommendationViewModel = ViewModelProvider(
            this@FavouritesActivity, recommendationFactory)[RecommendationViewModel::class.java]

        getRecommendedEstates()
    }

    private fun getFavourites() {
        val token = AppReferences.getToken(this@FavouritesActivity)
        allFavouritesViewModel.getFavourites(token)

        allFavouritesViewModel.getFavouritesLiveData.observe(this@FavouritesActivity) { response ->
            hideProgressDialog()
            response.let {
                favouritesAdapter = FavouritesAdapter(
                    it.wishlist.toMutableList()) { wishlist, _ ->
                    deleteFavourite(wishlist)
                }

                recyclerView.adapter = favouritesAdapter

                number_favourites.text = it.wishlist.size.toString()

                if (it.wishlist.isNotEmpty()) {
                    recycle_favourites.visibility = View.VISIBLE
                    iv_empty_favourites.visibility = View.GONE
                    tv_empty_favourites_title_2.visibility = View.GONE
                    tv_empty_favourites_title_3.visibility = View.GONE
                    tv_empty_favourites_title_4.visibility = View.GONE
                    tv_recommended_properties_fav.visibility = View.VISIBLE
                    rv_recommended_properties_you_may_like.visibility = View.VISIBLE
                } else {
                    recycle_favourites.visibility = View.GONE
                    iv_empty_favourites.visibility = View.VISIBLE
                    tv_empty_favourites_title_2.visibility = View.VISIBLE
                    tv_empty_favourites_title_3.visibility = View.VISIBLE
                    tv_empty_favourites_title_4.visibility = View.VISIBLE
                    tv_recommended_properties_fav.visibility = View.GONE
                    rv_recommended_properties_you_may_like.visibility = View.GONE
                }
            }
        }

        allFavouritesViewModel.errorLiveData.observe(this@FavouritesActivity) { error ->
            hideProgressDialog()
            error.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@FavouritesActivity, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(this@FavouritesActivity, "Error Server", Toast.LENGTH_LONG).show()
                }
            }
        }

        iv_delete_favourites.setOnClickListener {
            deleteAllFavourites()
        }
    }

    private fun deleteFavourite(wishlist: Wishlist) {
        if (networkUtils.isNetworkAvailable()) {
            showProgressDialog(this@FavouritesActivity, "please wait...")
            val token = AppReferences.getToken(this@FavouritesActivity)
            deleteFavouriteViewModel.deleteFavourite(token, wishlist._id)

            deleteFavouriteViewModel.deleteFavouriteLiveData.observe(this@FavouritesActivity) { response ->
                hideProgressDialog()
                response.let {
                    Log.e("deleteFavourite", it.message)

                    favouritesAdapter.removeFavourite(wishlist)

                    number_favourites.text = favouritesAdapter.itemCount.toString()

                    if (favouritesAdapter.itemCount == 0) {
                        recycle_favourites.visibility = View.GONE
                        iv_empty_favourites.visibility = View.VISIBLE
                        tv_empty_favourites_title_2.visibility = View.VISIBLE
                        tv_empty_favourites_title_3.visibility = View.VISIBLE
                        tv_empty_favourites_title_4.visibility = View.VISIBLE
                        tv_recommended_properties_fav.visibility = View.GONE
                        rv_recommended_properties_you_may_like.visibility = View.GONE
                    }
                }
            }

            deleteFavouriteViewModel.errorLiveData.observe(this@FavouritesActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@FavouritesActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@FavouritesActivity, "Error Server", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            showErrorSnackBar("No internet connection", true)
        }
    }

    private fun deleteAllFavourites() {
        val deleteAllFavouriteRepository = DeleteAllFavouriteRepository(RetrofitClient.instance)
        val deleteFactory = DeleteAllFavouriteFactory(deleteAllFavouriteRepository)
        deleteAllFavouriteViewModel = ViewModelProvider(this@FavouritesActivity, deleteFactory
        )[DeleteAllFavouriteViewModel::class.java]

        if (networkUtils.isNetworkAvailable()) {
            showProgressDialog(this@FavouritesActivity, "please wait...")
            val token = AppReferences.getToken(this@FavouritesActivity)
            deleteAllFavouriteViewModel.deleteAllFavourite(token)

            deleteAllFavouriteViewModel.deleteAllFavouriteLiveData.observe(this@FavouritesActivity) {response ->
                hideProgressDialog()
                response.let {
                    Toast.makeText(this@FavouritesActivity , it.message , Toast.LENGTH_LONG).show()
                    Log.e("deleteAllFavourite" , it.message)

                    number_favourites.text = "0"
                    recycle_favourites.visibility = View.GONE
                    iv_empty_favourites.visibility = View.VISIBLE
                    tv_empty_favourites_title_2.visibility = View.VISIBLE
                    tv_empty_favourites_title_3.visibility = View.VISIBLE
                    tv_empty_favourites_title_4.visibility = View.VISIBLE
                    tv_recommended_properties_fav.visibility = View.GONE
                    rv_recommended_properties_you_may_like.visibility = View.GONE
                }
            }

            deleteAllFavouriteViewModel.errorLiveData.observe(this@FavouritesActivity) {error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@FavouritesActivity , errorMessage , Toast.LENGTH_LONG).show()
                    }
                    catch (e:Exception){
                        Toast.makeText(this@FavouritesActivity , "Error Server" , Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            showErrorSnackBar("No internet connection", true)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRecommendedEstates() {
        val token = AppReferences.getToken(this@FavouritesActivity)
        val residenceId = "15"

        if (residenceId == null) {
            Log.e("DescriptionFragment", "Residence ID is null")
            return
        }

        recommendationViewModel.getRecommendation(token, residenceId)

        recommendationViewModel.getRecommendedEstatesResponseLiveData.observe(this@FavouritesActivity) { response ->
            hideProgressDialog()
            response?.let {
                val recommendedEstates: List<Data> = response.data
                recommendedPropertiesAdapter.list = recommendedEstates
                recommendedPropertiesAdapter.notifyDataSetChanged()

                Log.e("data" , recommendedEstates.toString())
            }
        }

        recommendationViewModel.errorLiveData.observe(this@FavouritesActivity) { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        this@FavouritesActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e("DescriptionFragment", "Recommendation Error: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(this@FavouritesActivity, error, Toast.LENGTH_LONG).show()
                    Log.e("DescriptionFragment", "Recommendation Error: $error")
                }
            }
        }
    }

    private fun handleFavouriteClick(residenceId: String, isLiked: Boolean) {
        if (!isLiked) {
            val token = AppReferences.getToken(this@FavouritesActivity)
            addToFavouritesViewModel.addToFavourites(token, residenceId)

            addToFavouritesViewModel.addToFavouritesLiveData.observe(this@FavouritesActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("AddToFavourites", "Added to Favourites $status")
                    recommendedPropertiesAdapter.updateFavouriteStatus(residenceId, true)

                    getFavourites()

                }
            }

            addToFavouritesViewModel.errorLiveData.observe(this@FavouritesActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@FavouritesActivity, errorMessage, Toast.LENGTH_LONG)
                            .show()
                    } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            val token = AppReferences.getToken(this@FavouritesActivity)
            deleteFavouriteViewModel.deleteFavourite(token, residenceId)

            deleteFavouriteViewModel.deleteFavouriteLiveData.observe(this@FavouritesActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("DeleteFavourite", "Deleted from Favourites $status")
                    recommendedPropertiesAdapter.updateFavouriteStatus(residenceId, false)
                }
            }

            deleteFavouriteViewModel.errorLiveData.observe(this@FavouritesActivity) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@FavouritesActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
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