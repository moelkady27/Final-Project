package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.update_listing.adapter.UpdateListingPhotosAdapter
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_update_residence.apartment_location_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.apartment_name_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.apartment_price_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.btn_next_update_listing
import kotlinx.android.synthetic.main.activity_update_residence.chip_apartment_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_cottage_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_hotel_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_house_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_rent_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_sell_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_villa_update
import kotlinx.android.synthetic.main.activity_update_residence.et_home_name_update_listing
import kotlinx.android.synthetic.main.activity_update_residence.image_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.toolbar_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.tv_apartment_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.tv_update_residence_3
import org.json.JSONException
import org.json.JSONObject

class UpdateResidenceActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: UpdateListingPhotosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_residence)

        recyclerView = findViewById(R.id.recyclerView_update_listing_photos)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = UpdateListingPhotosAdapter()
        recyclerView.adapter = adapter

        val residenceId = intent.getStringExtra("residence_id")
        Log.e("Residence ID", residenceId.toString())


        btn_next_update_listing.setOnClickListener {
            val intent = Intent(
                this@UpdateResidenceActivity, FirstUpdateActivity::class.java)
            startActivity(intent)
        }

        networkUtils = NetworkUtils(this@UpdateResidenceActivity)

        initChips()

        initView()

        setUpActionBar()
    }

    private fun initView() {
//        ********************* getting residence ****************

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factoryGetResidence = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@UpdateResidenceActivity,
            factoryGetResidence
        )[GetResidenceViewModel::class.java]

        getResidence()
    }

    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@UpdateResidenceActivity)
            val id = intent.getStringExtra("residence_id").toString()

            showProgressDialog(this@UpdateResidenceActivity , "please wait...")
            getResidenceViewModel.getResidence(token, id)

            getResidenceViewModel.getResidenceResponseLiveData.observe(this@UpdateResidenceActivity) {response ->
                hideProgressDialog()
                response.let {

                    if (response.residence.images.isNotEmpty()) {
                        Glide
                            .with(this)
                            .load(response.residence.images[0].url)
                            .into(image_update_residence)
                    } else {
                        Log.e("UpdateResidenceActivity", "No images found for this residence")
                    }

                    tv_apartment_update_residence.text = response.residence.category
//                    number_star_update_residence
                    apartment_name_update_residence.text = response.residence.title
                    apartment_location_update_residence.text = response.residence.location.fullAddress
                    apartment_price_update_residence.text = response.residence.salePrice.toString()
                    tv_update_residence_3.text = response.residence.paymentPeriod
//                    iv_update_residence_fav

                    et_home_name_update_listing.setText(response.residence.title)

                    when (response.residence.type) {
                        "rent" -> {
                            chip_rent_update.isChecked = true
                            chip_sell_update.isChecked = false
                        }
                        "sale" -> {
                            chip_sell_update.isChecked = true
                            chip_rent_update.isChecked = false
                        }
                    }

                    when (response.residence.category) {
                        "house" -> chip_house_update.isChecked = true
                        "apartment" -> chip_apartment_update.isChecked = true
                        "hotel" -> chip_hotel_update.isChecked = true
                        "villa" -> chip_villa_update.isChecked = true
                        "cottage" -> chip_cottage_update.isChecked = true
                    }
                    
                }

                getResidenceViewModel.errorLiveData.observe(this@UpdateResidenceActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(this@UpdateResidenceActivity, errorMessage, Toast.LENGTH_LONG)
                                .show()

                            Log.e("CompleteSignUpActivity", "Complete Error: $errorMessage")

                        } catch (e: JSONException) {
                            Toast.makeText(this@UpdateResidenceActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        else {
            showErrorSnackBar("No internet connection", true)
        }
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.chip_rent_update, R.id.chip_sell_update,

            R.id.chip_house_update, R.id.chip_apartment_update, R.id.chip_hotel_update,
            R.id.chip_villa_update, R.id.chip_cottage_update,
        )

        chipIds.forEach { chipId ->
            findViewById<Chip>(chipId).setOnCheckedChangeListener { chip, _ ->
                toggleChipSelection(chip as Chip)
            }
        }
    }

    private fun toggleChipSelection(chip: Chip) {
        chip.isSelected = !chip.isSelected
        if (chip.isSelected) {
            chip.setTextColor(Color.WHITE)
            chip.setChipBackgroundColorResource(R.color.colorPrimary)
            Log.e("SelectedChip", "Selected: ${chip.text}")
        } else {
            chip.setTextColor(resources.getColor(R.color.colorPrimaryText))
            chip.setChipBackgroundColorResource(R.color.home_search)
            Log.e("SelectedChip", "Deselected: ${chip.text}")
        }
    }


    private fun setUpActionBar() {
        setSupportActionBar(toolbar_update_residence)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_update_residence.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}