package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.factory.CreateResidenceFactory
import com.example.finalproject.ui.add_listing.repository.CreateResidenceRepository
import com.example.finalproject.ui.add_listing.viewModel.CreateResidenceViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_create_residence.btn_next_form_detail
import kotlinx.android.synthetic.main.activity_create_residence.chip_apartment
import kotlinx.android.synthetic.main.activity_create_residence.chip_cottage
import kotlinx.android.synthetic.main.activity_create_residence.chip_hotel
import kotlinx.android.synthetic.main.activity_create_residence.chip_house
import kotlinx.android.synthetic.main.activity_create_residence.chip_rent
import kotlinx.android.synthetic.main.activity_create_residence.chip_sell
import kotlinx.android.synthetic.main.activity_create_residence.chip_villa
import kotlinx.android.synthetic.main.activity_create_residence.et_home_name_add_listing
import kotlinx.android.synthetic.main.activity_create_residence.toolbar_form_detail
import org.json.JSONException
import org.json.JSONObject

class CreateResidenceActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var createResidenceVewModel: CreateResidenceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_residence)

        networkUtils = NetworkUtils(this@CreateResidenceActivity)

        initView()

        initChips()

        setUpActionBar()
    }

    private fun initView() {
        val createResidenceRepository = CreateResidenceRepository(RetrofitClient.instance)
        val factory = CreateResidenceFactory(createResidenceRepository)
        createResidenceVewModel = ViewModelProvider(
            this@CreateResidenceActivity,
            factory
        )[CreateResidenceViewModel::class.java]

        btn_next_form_detail.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                createResidence()
            }
            else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun createResidence() {
        val title = et_home_name_add_listing.text.toString().trim()
        val type = when {
            chip_rent.isSelected -> "Rent"
            chip_sell.isSelected -> "Sale"
            else -> ""
        }
        val category = when {
            chip_house.isSelected -> "House"
            chip_apartment.isSelected -> "Apartment"
            chip_hotel.isSelected -> "Hotel"
            chip_villa.isSelected -> "Villa"
            chip_cottage.isSelected -> "Cottage"
            else -> ""
        }

        val token = AppReferences.getToken(this@CreateResidenceActivity)

        if (isValidInput()) {
            showProgressDialog(this@CreateResidenceActivity , "Please Wait...")
            createResidenceVewModel.createResidence(token, title, type, category)

            createResidenceVewModel.createResidenceLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status

                    val residenceId = response.residence._id

                    val residence_Id = response.residence.Id

                    Log.e("CreateResidenceActivity", "Status: $status")

                    val intent = Intent(this@CreateResidenceActivity, AddListingLocationActivity::class.java)
                    intent.putExtra("residenceId", residenceId)
                    intent.putExtra("residence_Id", residence_Id)
                    Log.e("residenceId" , "create send $residenceId")
                    Log.e("residence_Id", "Create send $residence_Id")
                    startActivity(intent)
                }
            }
            createResidenceVewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@CreateResidenceActivity, errorMessage, Toast.LENGTH_LONG)
                            .show()

                        Log.e("CreateResidenceActivity", "Complete Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(this@CreateResidenceActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val title = et_home_name_add_listing.text.toString().trim()
        val type = when {
            chip_rent.isSelected -> "Rent"
            chip_sell.isSelected -> "Sale"
            else -> ""
        }
        val category = when {
            chip_house.isSelected -> "House"
            chip_apartment.isSelected -> "Apartment"
            chip_hotel.isSelected -> "Hotel"
            chip_villa.isSelected -> "Villa"
            chip_cottage.isSelected -> "Cottage"
            else -> ""
        }

        if (title.isEmpty()) {
            et_home_name_add_listing.error = "Title is not allowed to be empty."
            return false
        }

        if (type.isEmpty()) {
            showErrorSnackBarResidence("Please Select Listing Type", true)
            return false
        }

        if (category.isEmpty()) {
            showErrorSnackBarResidence("Please Select Property Category", true)
            return false
        }

        return true
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.chip_house, R.id.chip_apartment, R.id.chip_hotel,
            R.id.chip_villa, R.id.chip_cottage,

            R.id.chip_rent, R.id.chip_sell
        )

        chipIds.forEach { chipId ->
            findViewById<Chip>(chipId).setOnCheckedChangeListener { chip, _ ->
                toggleChipSelection(chip as Chip)
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_form_detail)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_form_detail.setNavigationOnClickListener {
            onBackPressed()
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

}