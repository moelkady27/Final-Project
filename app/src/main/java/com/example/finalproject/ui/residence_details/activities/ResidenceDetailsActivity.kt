package com.example.finalproject.ui.residence_details.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.residence_details.adapter.DetailsViewAdapter
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_residence_details.apartment_location_residence_detailss
import kotlinx.android.synthetic.main.activity_residence_details.apartment_residence_title_details
import kotlinx.android.synthetic.main.activity_residence_details.number_star_residence_details
import kotlinx.android.synthetic.main.activity_residence_details.residence_image_details
import kotlinx.android.synthetic.main.activity_residence_details.sale_type_residence_details
import kotlinx.android.synthetic.main.activity_residence_details.tabLayout_residence_details
import kotlinx.android.synthetic.main.activity_residence_details.tv_apartment_residence_category_details
import kotlinx.android.synthetic.main.activity_update_residence.image_update_residence
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

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("ResidenceDetailsActivity", "residenceId: $residenceId")

        viewPager2 = findViewById(R.id.view_pager_residence_details)

        adapter = DetailsViewAdapter(supportFragmentManager, lifecycle, residenceId!!)
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