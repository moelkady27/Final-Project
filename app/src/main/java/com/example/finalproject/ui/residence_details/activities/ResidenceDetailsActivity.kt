package com.example.finalproject.ui.residence_details.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.booking.activities.AcceptCancelBookedActivity
import com.example.finalproject.ui.residence_details.adapter.DetailsViewAdapter
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_residence_details.apartment_location_residence_detailss
import kotlinx.android.synthetic.main.activity_residence_details.apartment_residence_title_details
import kotlinx.android.synthetic.main.activity_residence_details.booked_by
import kotlinx.android.synthetic.main.activity_residence_details.btn_add_review
import kotlinx.android.synthetic.main.activity_residence_details.btn_book_now
import kotlinx.android.synthetic.main.activity_residence_details.con_total_and_book_now
import kotlinx.android.synthetic.main.activity_residence_details.number_star_residence_details
import kotlinx.android.synthetic.main.activity_residence_details.residence_image_details
import kotlinx.android.synthetic.main.activity_residence_details.sale_type_residence_details
import kotlinx.android.synthetic.main.activity_residence_details.tabLayout_residence_details
import kotlinx.android.synthetic.main.activity_residence_details.tv_apartment_residence_category_details
import kotlinx.android.synthetic.main.activity_residence_details.tv_price_number_2
import kotlinx.android.synthetic.main.activity_residence_details.tv_price_number_4
import kotlinx.android.synthetic.main.activity_residence_details.view_pager_residence_details
import org.json.JSONException
import org.json.JSONObject

class ResidenceDetailsActivity : BaseActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: DetailsViewAdapter

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    private lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_residence_details)


//        val residenceId = intent.getStringExtra("residenceId")
//        residenceId?.let {
//            Log.e("residenceId", it)
//        } ?: run {
//            Log.e("ResidenceDetailsActivity", "residenceId is null")
//        }
//
//        val residence_Id = intent.getStringExtra("residence_Id")
//        residence_Id?.let {
//            Log.e("Id", it)
//        } ?: run {
//            Log.e("ResidenceDetailsActivity", "residence_Id is null")
//        }

        val residenceId = intent.getStringExtra("residenceId")
        if (residenceId == null) {
            Log.e("ResidenceDetailsActivity", "residenceId is null")
            finish()
            return
        }

        val residence_Id = intent.getStringExtra("residence_Id")
        if (residence_Id == null) {
            Log.e("ResidenceDetailsActivity", "residence_Id is null")
            finish()
            return
        }

        viewPager2 = findViewById(R.id.view_pager_residence_details)

        adapter = DetailsViewAdapter(supportFragmentManager, lifecycle, residenceId!!,residence_Id!!)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout_residence_details, viewPager2) { tab, position ->
            when (position) {
                0 ->
                    tab.text = "Description"
                1 ->
                    tab.text = "Gallery"
                2 ->
                    tab.text = "Review"
            }
        }.attach()

        view_pager_residence_details.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    btn_add_review.visibility = View.VISIBLE
                } else {
                    btn_add_review.visibility = View.GONE
                }

                if (position == 0 || position == 1) {
                    con_total_and_book_now.visibility = View.VISIBLE
                } else {
                    con_total_and_book_now.visibility = View.GONE
                }

            }
        })

        btn_add_review.setOnClickListener {
            val intent = Intent(
                this@ResidenceDetailsActivity, AddReviewActivity::class.java)
            intent.putExtra("residenceId", residenceId)
            startActivity(intent)
        }

        btn_book_now.setOnClickListener {
            val intent = Intent(
                this@ResidenceDetailsActivity, PredictedPriceDetailsActivity::class.java)
            intent.putExtra("residenceId", residenceId)
            startActivity(intent)
        }


        networkUtils = NetworkUtils(this@ResidenceDetailsActivity)

        initView()
    }

    private fun initView() {
        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factoryGetResidence = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@ResidenceDetailsActivity,
            factoryGetResidence
        )[GetResidenceViewModel::class.java]

        getResidence()
    }

    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@ResidenceDetailsActivity)
            val id = intent.getStringExtra("residenceId")

            showProgressDialog(this@ResidenceDetailsActivity , "please wait...")
            getResidenceViewModel.getResidence(token, id!!)

            getResidenceViewModel.getResidenceResponseLiveData.observe(this@ResidenceDetailsActivity) {response ->
                hideProgressDialog()
                response.let {

                    if (response.residence.images.isNotEmpty()) {
                        Glide
                            .with(this)
                            .load(response.residence.images[0].url)
                            .into(residence_image_details)
                    } else {
                        Log.e("UpdateResidenceActivity", "No images found for this residence")
                    }

                    number_star_residence_details.text = response.residence.avgRating.toString()

                    tv_apartment_residence_category_details.text = response.residence.category

                    apartment_residence_title_details.text = response.residence.title

                    apartment_location_residence_detailss.text = response.residence.location.fullAddress

                    sale_type_residence_details.text = response.residence.type

                    tv_price_number_2.text = response.residence.salePrice

                    tv_price_number_4.text = response.residence.paymentPeriod

                    val getCurrentUserId = AppReferences.getUserId(this@ResidenceDetailsActivity)
                    Log.e("ResidenceDetailsActivity", "Current User Id: $getCurrentUserId")
                    val ownerId = response.residence.ownerId?._id
                    if (ownerId != null) {
                        Log.e("ResidenceDetailsActivity", "Owner Id: $ownerId")

                        if (getCurrentUserId == ownerId) {
                            booked_by.visibility = View.VISIBLE
                        } else {
                            booked_by.visibility = View.GONE
                        }
                    } else {
                        Log.e("ResidenceDetailsActivity", "Owner Id is null")
                    }

                    booked_by.setOnClickListener{
                        val intent = Intent(this@ResidenceDetailsActivity, AcceptCancelBookedActivity::class.java)
                        intent.putExtra("Residence Id", id)
                        startActivity(intent)
                    }
                }

                getResidenceViewModel.errorLiveData.observe(this@ResidenceDetailsActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(this@ResidenceDetailsActivity, errorMessage, Toast.LENGTH_LONG)
                                .show()

                            Log.e("CompleteSignUpActivity", "Complete Error: $errorMessage")

                        } catch (e: JSONException) {
                            Toast.makeText(this@ResidenceDetailsActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        else {
            showErrorSnackBar("No internet connection", true)
        }
    }
}