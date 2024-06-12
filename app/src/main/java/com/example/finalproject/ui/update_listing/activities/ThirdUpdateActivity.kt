package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_average_update
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_good_update
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_minimum_update
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_no_exposure_update
import kotlinx.android.synthetic.main.activity_third_update.btn_next_third_update
import kotlinx.android.synthetic.main.activity_third_update.con_fire_places_update
import kotlinx.android.synthetic.main.activity_third_update.con_garage_cars_update
import kotlinx.android.synthetic.main.activity_third_update.condition_of_basement_average_update
import kotlinx.android.synthetic.main.activity_third_update.condition_of_basement_excellent_update
import kotlinx.android.synthetic.main.activity_third_update.condition_of_basement_fair_update
import kotlinx.android.synthetic.main.activity_third_update.condition_of_basement_good_update
import kotlinx.android.synthetic.main.activity_third_update.condition_of_basement_not_available_update
import kotlinx.android.synthetic.main.activity_third_update.condition_of_basement_poor_update
import kotlinx.android.synthetic.main.activity_third_update.count_bathrooms_update
import kotlinx.android.synthetic.main.activity_third_update.count_bedrooms_update
import kotlinx.android.synthetic.main.activity_third_update.count_fire_places_update
import kotlinx.android.synthetic.main.activity_third_update.count_garage_cars_update
import kotlinx.android.synthetic.main.activity_third_update.count_kitchen_update
import kotlinx.android.synthetic.main.activity_third_update.count_rooms_without_bathrooms_update
import kotlinx.android.synthetic.main.activity_third_update.et_basement_area_update
import kotlinx.android.synthetic.main.activity_third_update.fire_place_quality_average_update
import kotlinx.android.synthetic.main.activity_third_update.fire_place_quality_excellent_update
import kotlinx.android.synthetic.main.activity_third_update.fire_place_quality_fair_update
import kotlinx.android.synthetic.main.activity_third_update.fire_place_quality_good_update
import kotlinx.android.synthetic.main.activity_third_update.fire_place_quality_poor_update
import kotlinx.android.synthetic.main.activity_third_update.garage_finish_finished_update
import kotlinx.android.synthetic.main.activity_third_update.garage_finish_not_available_update
import kotlinx.android.synthetic.main.activity_third_update.garage_finish_rough_finished_update
import kotlinx.android.synthetic.main.activity_third_update.garage_finish_unfinished_update
import kotlinx.android.synthetic.main.activity_third_update.garage_quality_average_update
import kotlinx.android.synthetic.main.activity_third_update.garage_quality_excellent_update
import kotlinx.android.synthetic.main.activity_third_update.garage_quality_fair_update
import kotlinx.android.synthetic.main.activity_third_update.garage_quality_good_update
import kotlinx.android.synthetic.main.activity_third_update.garage_quality_poor_update
import kotlinx.android.synthetic.main.activity_third_update.garage_type_attached_update
import kotlinx.android.synthetic.main.activity_third_update.garage_type_basement_update
import kotlinx.android.synthetic.main.activity_third_update.garage_type_built_in_update
import kotlinx.android.synthetic.main.activity_third_update.garage_type_car_port_update
import kotlinx.android.synthetic.main.activity_third_update.garage_type_detached_update
import kotlinx.android.synthetic.main.activity_third_update.garage_type_more_than_one_update
import kotlinx.android.synthetic.main.activity_third_update.garage_type_not_available_update
import kotlinx.android.synthetic.main.activity_third_update.height_of_basement_average_update
import kotlinx.android.synthetic.main.activity_third_update.height_of_basement_excellent_update
import kotlinx.android.synthetic.main.activity_third_update.height_of_basement_fair_update
import kotlinx.android.synthetic.main.activity_third_update.height_of_basement_good_update
import kotlinx.android.synthetic.main.activity_third_update.height_of_basement_poor_update
import kotlinx.android.synthetic.main.activity_third_update.hsv_condition_of_basement_update
import kotlinx.android.synthetic.main.activity_third_update.hsv_fire_place_quality_update
import kotlinx.android.synthetic.main.activity_third_update.kitchen_quality_average_update
import kotlinx.android.synthetic.main.activity_third_update.kitchen_quality_excellent_update
import kotlinx.android.synthetic.main.activity_third_update.kitchen_quality_fair_update
import kotlinx.android.synthetic.main.activity_third_update.kitchen_quality_good_update
import kotlinx.android.synthetic.main.activity_third_update.kitchen_quality_poor_update
import kotlinx.android.synthetic.main.activity_third_update.radio_group_basement_update
import kotlinx.android.synthetic.main.activity_third_update.radio_group_fire_place_update
import kotlinx.android.synthetic.main.activity_third_update.radio_group_garage_update
import kotlinx.android.synthetic.main.activity_third_update.rating_of_basement_average_update
import kotlinx.android.synthetic.main.activity_third_update.rating_of_basement_below_average_update
import kotlinx.android.synthetic.main.activity_third_update.rating_of_basement_good_update
import kotlinx.android.synthetic.main.activity_third_update.rating_of_basement_low_update
import kotlinx.android.synthetic.main.activity_third_update.rating_of_basement_rec_room_update
import kotlinx.android.synthetic.main.activity_third_update.rating_of_basement_unfinished_update
import kotlinx.android.synthetic.main.activity_third_update.toolbar_third_update
import kotlinx.android.synthetic.main.activity_third_update.tv_basement_area_update
import kotlinx.android.synthetic.main.activity_third_update.tv_basement_exposure_update
import kotlinx.android.synthetic.main.activity_third_update.tv_condition_of_basement_update
import kotlinx.android.synthetic.main.activity_third_update.tv_fire_place_quality_update
import kotlinx.android.synthetic.main.activity_third_update.tv_garage_cars_update_1
import kotlinx.android.synthetic.main.activity_third_update.tv_garage_finish_update
import kotlinx.android.synthetic.main.activity_third_update.tv_garage_quality_update
import kotlinx.android.synthetic.main.activity_third_update.tv_garage_type_update
import kotlinx.android.synthetic.main.activity_third_update.tv_height_of_basement_update
import kotlinx.android.synthetic.main.activity_third_update.tv_rating_of_basement_update

class ThirdUpdateActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_update)

        networkUtils = NetworkUtils(this@ThirdUpdateActivity)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("Residence ID third", residenceId.toString())

        initChips()

        initView()

        setUpActionBar()
    }

    private fun initView() {

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factory = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@ThirdUpdateActivity,
            factory
        )[GetResidenceViewModel::class.java]

        getResidence()

        btn_next_third_update.setOnClickListener {
            val intent = Intent(
                this@ThirdUpdateActivity, FourthUpdateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@ThirdUpdateActivity)
            val residenceId = intent.getStringExtra("residenceId").toString()

            Log.e("Residence ID", "ResidenceID: $residenceId")

            showProgressDialog(this@ThirdUpdateActivity, "please wait...")
            getResidenceViewModel.getResidence(token, residenceId)

            getResidenceViewModel.getResidenceResponseLiveData.observe(this@ThirdUpdateActivity) {response->
                hideProgressDialog()
                response.let {
                    if (response.residence.hasGarage) {
                        radio_group_garage_update.check(R.id.btn_yes_garage_update)

                        when (response.residence.garageType) {
                            "more than one" -> garage_type_more_than_one_update.isChecked = true
                            "attached" -> garage_type_attached_update.isChecked = true
                            "basement" -> garage_type_basement_update.isChecked = true
                            "built in" -> garage_type_built_in_update.isChecked = true
                            "car port" -> garage_type_car_port_update.isChecked = true
                            "detached" -> garage_type_detached_update.isChecked = true
                            "not available" -> garage_type_not_available_update.isChecked = true
                        }

                        when (response.residence.garageQual) {
                            "excellent" -> garage_quality_excellent_update.isChecked = true
                            "good" -> garage_quality_good_update.isChecked = true
                            "average" -> garage_quality_average_update.isChecked = true
                            "fair" -> garage_quality_fair_update.isChecked = true
                            "poor" -> garage_quality_poor_update.isChecked = true
                        }

                        count_garage_cars_update.text = response.residence.garageCars.toString()

                        when (response.residence.garageFinish) {
                            "finished" -> garage_finish_finished_update.isChecked = true
                            "rough finished" -> garage_finish_rough_finished_update.isChecked = true
                            "unfinished" -> garage_finish_unfinished_update.isChecked = true
                            "not available" -> garage_finish_not_available_update.isChecked = true
                        }

                        tv_garage_type_update.visibility = View.VISIBLE
                        garage_type_more_than_one_update.visibility = View.VISIBLE
                        garage_type_attached_update.visibility = View.VISIBLE
                        garage_type_basement_update.visibility = View.VISIBLE
                        garage_type_built_in_update.visibility = View.VISIBLE
                        garage_type_car_port_update.visibility = View.VISIBLE
                        garage_type_detached_update.visibility = View.VISIBLE
                        garage_type_not_available_update.visibility = View.VISIBLE

                        tv_garage_quality_update.visibility = View.VISIBLE
                        garage_quality_excellent_update.visibility = View.VISIBLE
                        garage_quality_good_update.visibility = View.VISIBLE
                        garage_quality_average_update.visibility = View.VISIBLE
                        garage_quality_fair_update.visibility = View.VISIBLE
                        garage_quality_poor_update.visibility = View.VISIBLE

                        tv_garage_finish_update.visibility = View.VISIBLE
                        garage_finish_finished_update.visibility = View.VISIBLE
                        garage_finish_rough_finished_update.visibility = View.VISIBLE
                        garage_finish_unfinished_update.visibility = View.VISIBLE
                        garage_finish_not_available_update.visibility = View.VISIBLE

                        tv_garage_cars_update_1.visibility = View.VISIBLE
                        con_garage_cars_update.visibility = View.VISIBLE

                    } else {
                        radio_group_garage_update.check(R.id.btn_no_garage_update)

                        tv_garage_type_update.visibility = View.GONE
                        garage_type_more_than_one_update.visibility = View.GONE
                        garage_type_attached_update.visibility = View.GONE
                        garage_type_basement_update.visibility = View.GONE
                        garage_type_built_in_update.visibility = View.GONE
                        garage_type_car_port_update.visibility = View.GONE
                        garage_type_detached_update.visibility = View.GONE
                        garage_type_not_available_update.visibility = View.GONE

                        tv_garage_quality_update.visibility = View.GONE
                        garage_quality_excellent_update.visibility = View.GONE
                        garage_quality_good_update.visibility = View.GONE
                        garage_quality_average_update.visibility = View.GONE
                        garage_quality_fair_update.visibility = View.GONE
                        garage_quality_poor_update.visibility = View.GONE

                        tv_garage_finish_update.visibility = View.GONE
                        garage_finish_finished_update.visibility = View.GONE
                        garage_finish_rough_finished_update.visibility = View.GONE
                        garage_finish_unfinished_update.visibility = View.GONE
                        garage_finish_not_available_update.visibility = View.GONE

                        tv_garage_cars_update_1.visibility = View.GONE
                        con_garage_cars_update.visibility = View.GONE
                    }

                    if (response.residence.hasBasement){
                        radio_group_basement_update.check(R.id.btn_yes_basement_update)
                        et_basement_area_update.setText(response.residence.bsmtUnfSF.toString())

                        when (response.residence.bsmtExposure) {
                            "good" -> basement_exposure_good_update.isChecked = true
                            "average" -> basement_exposure_average_update.isChecked = true
                            "minimum" -> basement_exposure_minimum_update.isChecked = true
                            "no exposure" -> basement_exposure_no_exposure_update.isChecked = true
                        }

//                        when (response.residence.bsmtFinType1) {
//
//                        }

                        when(response.residence.bsmtQual) {
                            "excellent" -> height_of_basement_excellent_update.isChecked = true
                            "good" -> height_of_basement_good_update.isChecked = true
                            "average" -> height_of_basement_average_update.isChecked = true
                            "fair" -> height_of_basement_fair_update.isChecked = true
                            "poor" -> height_of_basement_poor_update.isChecked = true
                        }

                        when(response.residence.bsmtCond) {
                            "excellent" -> condition_of_basement_excellent_update.isChecked = true
                            "good" -> condition_of_basement_good_update.isChecked = true
                            "average" -> condition_of_basement_average_update.isChecked = true
                            "fair" -> condition_of_basement_fair_update.isChecked = true
                            "poor" -> condition_of_basement_poor_update.isChecked = true
                            "not available" -> condition_of_basement_not_available_update.isChecked = true

                        }

                        tv_basement_area_update.visibility = View.VISIBLE
                        et_basement_area_update.visibility = View.VISIBLE

                        tv_basement_exposure_update.visibility = View.VISIBLE
                        basement_exposure_good_update.visibility = View.VISIBLE
                        basement_exposure_average_update.visibility = View.VISIBLE
                        basement_exposure_minimum_update.visibility = View.VISIBLE
                        basement_exposure_no_exposure_update.visibility = View.VISIBLE

                        tv_rating_of_basement_update.visibility = View.VISIBLE
                        rating_of_basement_good_update.visibility = View.VISIBLE
                        rating_of_basement_average_update.visibility = View.VISIBLE
                        rating_of_basement_below_average_update.visibility = View.VISIBLE
                        rating_of_basement_rec_room_update.visibility = View.VISIBLE
                        rating_of_basement_low_update.visibility = View.VISIBLE
                        rating_of_basement_unfinished_update.visibility = View.VISIBLE

                        tv_height_of_basement_update.visibility = View.VISIBLE
                        height_of_basement_excellent_update.visibility = View.VISIBLE
                        height_of_basement_good_update.visibility = View.VISIBLE
                        height_of_basement_average_update.visibility = View.VISIBLE
                        height_of_basement_fair_update.visibility = View.VISIBLE
                        height_of_basement_poor_update.visibility = View.VISIBLE

                        tv_condition_of_basement_update.visibility = View.VISIBLE
                        hsv_condition_of_basement_update.visibility = View.VISIBLE

                    }
                    else {
                        radio_group_basement_update.check(R.id.btn_no_basement_update)

                        tv_basement_area_update.visibility = View.GONE
                        et_basement_area_update.visibility = View.GONE

                        tv_basement_exposure_update.visibility = View.GONE
                        basement_exposure_good_update.visibility = View.GONE
                        basement_exposure_average_update.visibility = View.GONE
                        basement_exposure_minimum_update.visibility = View.GONE
                        basement_exposure_no_exposure_update.visibility = View.GONE

                        tv_rating_of_basement_update.visibility = View.GONE
                        rating_of_basement_good_update.visibility = View.GONE
                        rating_of_basement_average_update.visibility = View.GONE
                        rating_of_basement_below_average_update.visibility = View.GONE
                        rating_of_basement_rec_room_update.visibility = View.GONE
                        rating_of_basement_low_update.visibility = View.GONE
                        rating_of_basement_unfinished_update.visibility = View.GONE

                        tv_height_of_basement_update.visibility = View.GONE
                        height_of_basement_excellent_update.visibility = View.GONE
                        height_of_basement_good_update.visibility = View.GONE
                        height_of_basement_average_update.visibility = View.GONE
                        height_of_basement_fair_update.visibility = View.GONE
                        height_of_basement_poor_update.visibility = View.GONE

                        tv_condition_of_basement_update.visibility = View.GONE
                        hsv_condition_of_basement_update.visibility = View.GONE

                    }

                    if (response.residence.hasFireplace){
                        radio_group_fire_place_update.check(R.id.btn_yes_fire_place_update)
                        count_fire_places_update.text = response.residence.fireplaces.toString()

                        when(response.residence.fireplaceQu) {
                            "excellent" -> fire_place_quality_excellent_update.isChecked = true
                            "good" -> fire_place_quality_good_update.isChecked = true
                            "average" -> fire_place_quality_average_update.isChecked = true
                            "fair" -> fire_place_quality_fair_update.isChecked = true
                            "poor" -> fire_place_quality_poor_update.isChecked = true
                        }

                        con_fire_places_update.visibility = View.VISIBLE
                        tv_fire_place_quality_update.visibility = View.VISIBLE
                        hsv_fire_place_quality_update.visibility = View.VISIBLE
                    }
                    else {
                        radio_group_fire_place_update.check(R.id.btn_no_fire_place_update)

                        con_fire_places_update.visibility = View.GONE
                        tv_fire_place_quality_update.visibility = View.GONE
                        hsv_fire_place_quality_update.visibility = View.GONE
                    }

                    count_bedrooms_update.text = response.residence.bedroomAbvGr.toString()
                    count_bathrooms_update.text = response.residence.totalbaths.toString()
                    count_kitchen_update.text = response.residence.KitchenAbvGr.toString()

                    when (response.residence.kitchenQual) {
                        "excellent" -> kitchen_quality_excellent_update.isChecked = true
                        "good" -> kitchen_quality_good_update.isChecked = true
                        "average" -> kitchen_quality_average_update.isChecked = true
                        "fair" -> kitchen_quality_fair_update.isChecked = true
                        "poor" -> kitchen_quality_poor_update.isChecked = true
                    }

                    count_rooms_without_bathrooms_update.text = response.residence.totRmsAbvGrd.toString()
                }
            }
        }
    }

    private fun initChips() {
        val chipId = listOf(
            R.id.garage_type_more_than_one_update, R.id.garage_type_attached_update, R.id.garage_type_basement_update,
            R.id.garage_type_built_in_update, R.id.garage_type_car_port_update, R.id.garage_type_detached_update,
            R.id.garage_type_not_available_update,

            R.id.garage_quality_excellent_update, R.id.garage_quality_good_update,
            R.id.garage_quality_average_update, R.id.garage_quality_fair_update, R.id.garage_quality_poor_update ,

            R.id.garage_finish_finished_update, R.id.garage_finish_rough_finished_update,
            R.id.garage_finish_unfinished_update, R.id.garage_finish_not_available_update,

            R.id.basement_exposure_good_update, R.id.basement_exposure_average_update,
            R.id.basement_exposure_minimum_update, R.id.basement_exposure_no_exposure_update,

            R.id.rating_of_basement_good_update, R.id.rating_of_basement_average_update,
            R.id.rating_of_basement_below_average_update, R.id.rating_of_basement_rec_room_update,
            R.id.rating_of_basement_low_update, R.id.rating_of_basement_unfinished_update,

            R.id.height_of_basement_excellent_update, R.id.height_of_basement_good_update,
            R.id.height_of_basement_average_update, R.id.height_of_basement_fair_update, R.id.height_of_basement_poor_update,

            R.id.condition_of_basement_excellent_update, R.id.condition_of_basement_good_update,
            R.id.condition_of_basement_average_update, R.id.condition_of_basement_fair_update,
            R.id.condition_of_basement_poor_update, R.id.condition_of_basement_not_available_update,

            R.id.fire_place_quality_excellent_update, R.id.fire_place_quality_good_update, R.id.fire_place_quality_average_update,
            R.id.fire_place_quality_fair_update, R.id.fire_place_quality_poor_update,

            R.id.kitchen_quality_excellent_update, R.id.kitchen_quality_good_update, R.id.kitchen_quality_average_update,
            R.id.kitchen_quality_fair_update, R.id.kitchen_quality_poor_update,
        )

        chipId.forEach { chipId ->
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
        } else {
            chip.setTextColor(resources.getColor(R.color.colorPrimaryText))
            chip.setChipBackgroundColorResource(R.color.home_search)
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_third_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_third_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}