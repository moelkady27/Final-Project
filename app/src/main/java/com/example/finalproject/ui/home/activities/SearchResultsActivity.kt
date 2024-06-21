package com.example.finalproject.ui.home.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
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
import com.example.finalproject.ui.home.factory.FiltrationSearchFactory
import com.example.finalproject.ui.home.factory.HomeFeaturedEstatesFactory
import com.example.finalproject.ui.home.repository.FiltrationSearchRepository
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import com.example.finalproject.ui.home.viewModel.FiltrationSearchViewModel
import com.example.finalproject.ui.home.viewModel.HomeFeaturedEstatesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_search_results.et_search_results
import kotlinx.android.synthetic.main.activity_search_results.iv_filter_search_results
import kotlinx.android.synthetic.main.activity_search_results.toolbar_search_results
import kotlinx.android.synthetic.main.activity_third_complete.count_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.count_bedrooms
import org.json.JSONException
import org.json.JSONObject

class SearchResultsActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var recyclerView: RecyclerView

    private lateinit var searchResultsAdapter: SearchResultsAdapter

    private lateinit var homeFeaturedEstatesViewModel: HomeFeaturedEstatesViewModel

    private lateinit var addToFavouritesViewModel: AddToFavouritesViewModel

    private lateinit var deleteFavouriteViewModel: DeleteFavouriteViewModel

    private lateinit var filtrationSearchViewModel: FiltrationSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        networkUtils = NetworkUtils(this@SearchResultsActivity)

        iv_filter_search_results.setOnClickListener {
            showFilterDialog()
        }

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

                                /* Filtration-Search */

        val filtrationSearchRepository = FiltrationSearchRepository(RetrofitClient.instance)
        val filtrationSearchFactory = FiltrationSearchFactory(filtrationSearchRepository)
        filtrationSearchViewModel = ViewModelProvider(
            this@SearchResultsActivity, filtrationSearchFactory
        )[FiltrationSearchViewModel::class.java]
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

    private fun filtrationSearch(
        minPrice: String, maxPrice: String, rating: String,
        bedroom: String, bathroom: String, neighborhood: String
    ){
        val token = AppReferences.getToken(this@SearchResultsActivity)

        filtrationSearchViewModel.filtrationSearch(
            token, minPrice, maxPrice, rating, bedroom, bathroom, neighborhood
        )

        filtrationSearchViewModel.filtrationSearchLiveData.observe(
            this@SearchResultsActivity) { response ->
            hideProgressDialog()
            response?.let {
                val status = it.status
                Log.e("FiltrationSearch", "Status: $status")

                val itemCount = it.count
                Log.e("Count", "Count is $itemCount")
            }
        }

        filtrationSearchViewModel.errorLiveData.observe(this) { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        this@SearchResultsActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("FiltrationSearch", "Filtration Search Error: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(
                        this@SearchResultsActivity, error, Toast.LENGTH_LONG).show()
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

    private fun showFilterDialog() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        val filterDialogView = layoutInflater.inflate(R.layout.filter_search_dialog, null)
        filterDialogView.background = ContextCompat.getDrawable(
            this, R.drawable.message_options_dialog_background)

        val btnApply = filterDialogView.findViewById<AppCompatTextView>(R.id.btn_apply_filter)
        val btnCancel = filterDialogView.findViewById<AppCompatTextView>(R.id.btn_cancel_filter)

        val etMinPrice = filterDialogView.findViewById<EditText>(R.id.et_min_price)
        val etMaxPrice = filterDialogView.findViewById<EditText>(R.id.et_max_price)
        val bedroomTextView = filterDialogView.findViewById<TextView>(R.id.count_bedroom_filter)
        val bathroomTextView = filterDialogView.findViewById<TextView>(R.id.count_bathroom_filter)
        val neighborhoodAutoComplete = filterDialogView.findViewById<AutoCompleteTextView>(
            R.id.auto_complete_location_filter
        )

        val plusBedroom = filterDialogView.findViewById<Button>(R.id.plus_bedroom_filter)
        val minusBedroom = filterDialogView.findViewById<Button>(R.id.minus_bedroom_filter)
        val plusBathroom = filterDialogView.findViewById<Button>(R.id.plus_bathroom_filter)
        val minusBathroom = filterDialogView.findViewById<Button>(R.id.minus_bathroom_filter)

        plusBedroom.setOnClickListener {
            val currentText = bedroomTextView.text.toString()
            val currentNumber = currentText.toInt()
            val newNumber = currentNumber + 1
            bedroomTextView.text = newNumber.toString()
            Log.e("Count Bedroom Filter", newNumber.toString())
        }

        minusBedroom.setOnClickListener {
            val currentText = bedroomTextView.text.toString()
            val currentNumber = currentText.toInt()
            if (currentNumber > 0) {
                val newNumber = currentNumber - 1
                bedroomTextView.text = newNumber.toString()
                Log.e("Count Bedroom Filter", newNumber.toString())
            } else {
                Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
            }
        }

        plusBathroom.setOnClickListener {
            val currentText = bathroomTextView.text.toString()
            val currentNumber = currentText.toInt()
            val newNumber = currentNumber + 1
            bathroomTextView.text = newNumber.toString()
            Log.e("Count Bathroom Filter", newNumber.toString())
        }

        minusBathroom.setOnClickListener {
            val currentText = bathroomTextView.text.toString()
            val currentNumber = currentText.toInt()
            if (currentNumber > 0) {
                val newNumber = currentNumber - 1
                bathroomTextView.text = newNumber.toString()
                Log.e("Count Bathroom Filter", newNumber.toString())
            } else {
                Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
            }
        }

        val locationItems = resources.getStringArray(R.array.neighborhood_items)
        val arrayLocationAdapter = ArrayAdapter(
            this@SearchResultsActivity, R.layout.dropdown_item, locationItems
        )
        neighborhoodAutoComplete.setAdapter(arrayLocationAdapter)
        neighborhoodAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedLocation", "Selected Location: $selectedItem")
        }

        btnApply.setOnClickListener {
            val minPrice = etMinPrice.text.toString().trim()
            val maxPrice = etMaxPrice.text.toString().trim()
            val rating = "0"
            val bedroom = bedroomTextView.text.toString().trim()
            val bathroom = bathroomTextView.text.toString().trim()
            val neighborhood = neighborhoodAutoComplete.text.toString().trim()

            filtrationSearch(minPrice, maxPrice, rating, bedroom, bathroom, neighborhood)
            bottomSheetDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setContentView(filterDialogView)
        bottomSheetDialog.show()
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