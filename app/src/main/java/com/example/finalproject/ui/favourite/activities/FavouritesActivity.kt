package com.example.finalproject.ui.favourite.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.favourite.adapter.FavouritesAdapter
import com.example.finalproject.ui.favourite.factory.AllFavouritesFactory
import com.example.finalproject.ui.favourite.factory.DeleteAllFavouriteFactory
import com.example.finalproject.ui.favourite.repository.AllFavouritesRepository
import com.example.finalproject.ui.favourite.repository.DeleteAllFavouriteRepository
import com.example.finalproject.ui.favourite.viewModel.AllFavouritesViewModel
import com.example.finalproject.ui.favourite.viewModel.DeleteAllFavouriteViewModel
import kotlinx.android.synthetic.main.activity_favourites.iv_delete_favourites
import kotlinx.android.synthetic.main.activity_favourites.iv_empty_favourites
import kotlinx.android.synthetic.main.activity_favourites.number_favourites
import kotlinx.android.synthetic.main.activity_favourites.recycle_favourites
import kotlinx.android.synthetic.main.activity_favourites.toolbar_favourites
import kotlinx.android.synthetic.main.activity_favourites.tv_empty_favourites_title_2
import kotlinx.android.synthetic.main.activity_favourites.tv_empty_favourites_title_3
import kotlinx.android.synthetic.main.activity_favourites.tv_empty_favourites_title_4
import org.json.JSONObject

class FavouritesActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var favouritesAdapter: FavouritesAdapter

    private lateinit var allFavouritesViewModel: AllFavouritesViewModel

    private lateinit var deleteAllFavouriteViewModel: DeleteAllFavouriteViewModel

    private lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        setUpActionBar()

        recyclerView = findViewById(R.id.recycle_favourites)
        recyclerView.layoutManager = LinearLayoutManager(this@FavouritesActivity , LinearLayoutManager.VERTICAL , false)

        networkUtils = NetworkUtils(this@FavouritesActivity)

        initView()
    }

    private fun initView() {
        val allFavouritesRepository = AllFavouritesRepository(RetrofitClient.instance)
        val factory = AllFavouritesFactory(allFavouritesRepository)
        allFavouritesViewModel = ViewModelProvider(this@FavouritesActivity, factory
        )[AllFavouritesViewModel::class.java]

        if (networkUtils.isNetworkAvailable()) {
            showProgressDialog(this@FavouritesActivity, "please wait...")
            getFavourites()
        } else {
            showErrorSnackBar("No internet connection", true)
        }
    }

    private fun getFavourites() {
        val token = AppReferences.getToken(this@FavouritesActivity)
        allFavouritesViewModel.getFavourites(token)

        allFavouritesViewModel.getFavouritesLiveData.observe(this@FavouritesActivity) {response ->
            hideProgressDialog()
            response.let {
                favouritesAdapter = FavouritesAdapter(it.wishlist)
                recyclerView.adapter = favouritesAdapter

                number_favourites.text = it.wishlist.size.toString()

                if (it.wishlist.isNotEmpty()) {
                    recycle_favourites.visibility = View.VISIBLE
                    iv_empty_favourites.visibility = View.GONE
                    tv_empty_favourites_title_2.visibility = View.GONE
                    tv_empty_favourites_title_3.visibility = View.GONE
                    tv_empty_favourites_title_4.visibility = View.GONE

                } else {
                    recycle_favourites.visibility = View.GONE
                    iv_empty_favourites.visibility = View.VISIBLE
                    tv_empty_favourites_title_2.visibility = View.VISIBLE
                    tv_empty_favourites_title_3.visibility = View.VISIBLE
                    tv_empty_favourites_title_4.visibility = View.VISIBLE
                }
            }
        }

        allFavouritesViewModel.errorLiveData.observe(this@FavouritesActivity) {error ->
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

        iv_delete_favourites.setOnClickListener {
            deleteAllFavourites()
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
                    number_favourites.text = "0"
                    recycle_favourites.visibility = View.GONE
                    iv_empty_favourites.visibility = View.VISIBLE
                    tv_empty_favourites_title_2.visibility = View.VISIBLE
                    tv_empty_favourites_title_3.visibility = View.VISIBLE
                    tv_empty_favourites_title_4.visibility = View.VISIBLE
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