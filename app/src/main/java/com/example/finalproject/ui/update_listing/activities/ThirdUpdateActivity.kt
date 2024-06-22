package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.factory.ThirdUpdateFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.repository.ThirdUpdateRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.example.finalproject.ui.update_listing.viewModel.ThirdUpdateViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_average_update
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_good_update
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_minimum_update
import kotlinx.android.synthetic.main.activity_third_update.basement_exposure_no_exposure_update
import kotlinx.android.synthetic.main.activity_third_update.btn_next_third_update
import kotlinx.android.synthetic.main.activity_third_update.btn_no_basement_update
import kotlinx.android.synthetic.main.activity_third_update.btn_no_fire_place_update
import kotlinx.android.synthetic.main.activity_third_update.btn_no_garage_update
import kotlinx.android.synthetic.main.activity_third_update.btn_yes_basement_update
import kotlinx.android.synthetic.main.activity_third_update.btn_yes_fire_place_update
import kotlinx.android.synthetic.main.activity_third_update.btn_yes_garage_update
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
import kotlinx.android.synthetic.main.activity_third_update.minus_bathrooms_update
import kotlinx.android.synthetic.main.activity_third_update.minus_bedrooms_update
import kotlinx.android.synthetic.main.activity_third_update.minus_fire_places_update
import kotlinx.android.synthetic.main.activity_third_update.minus_garage_cars_update
import kotlinx.android.synthetic.main.activity_third_update.minus_kitchen_update
import kotlinx.android.synthetic.main.activity_third_update.minus_rooms_without_bathrooms_update
import kotlinx.android.synthetic.main.activity_third_update.plus_bathrooms_update
import kotlinx.android.synthetic.main.activity_third_update.plus_bedrooms_update
import kotlinx.android.synthetic.main.activity_third_update.plus_fire_places_update
import kotlinx.android.synthetic.main.activity_third_update.plus_garage_cars_update
import kotlinx.android.synthetic.main.activity_third_update.plus_kitchen_update
import kotlinx.android.synthetic.main.activity_third_update.plus_rooms_without_bathrooms_update
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
import org.json.JSONException
import org.json.JSONObject

class ThirdUpdateActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    private lateinit var thirdUpdateViewModel: ThirdUpdateViewModel

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
                                    /* Get-Residence */

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factory = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@ThirdUpdateActivity,
            factory
        )[GetResidenceViewModel::class.java]

        getResidence()

                                /* Update-Third-Residence */

        val thirdUpdateRepository = ThirdUpdateRepository(RetrofitClient.instance)
        val factoryThirdUpdate = ThirdUpdateFactory(thirdUpdateRepository)
        thirdUpdateViewModel = ViewModelProvider(
            this@ThirdUpdateActivity,
            factoryThirdUpdate
        )[ThirdUpdateViewModel::class.java]

        initVisibility()

        initButtons()

        btn_next_third_update.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                thirdUpdate()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
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

    private fun thirdUpdate() {
        val hasGarage: String
        val garageType: String
        val garageQuality: String
        val garageCars: String
        val garageFinish: String

        if (btn_yes_garage_update.isChecked) {
            hasGarage = "true"
            garageType = when {
                garage_type_more_than_one_update.isChecked -> "more than one"
                garage_type_attached_update.isChecked -> "attached"
                garage_type_basement_update.isChecked -> "basement"
                garage_type_built_in_update.isChecked -> "built in"
                garage_type_car_port_update.isChecked -> "car port"
                garage_type_detached_update.isChecked -> "detached"
                garage_type_not_available_update.isChecked -> "not available"
                else -> ""
            }
            garageQuality = when {
                garage_quality_excellent_update.isChecked -> "excellent"
                garage_quality_good_update.isChecked -> "good"
                garage_quality_average_update.isChecked -> "average"
                garage_quality_fair_update.isChecked -> "fair"
                garage_quality_poor_update.isChecked -> "poor"
                else -> ""
            }

            garageCars = count_garage_cars_update.text.toString().trim()

            garageFinish = when {
                garage_finish_finished_update.isChecked -> "finished"
                garage_finish_rough_finished_update.isChecked -> "rough finished"
                garage_finish_unfinished_update.isChecked -> "unfinished"
                garage_finish_not_available_update.isChecked -> "not available"
                else -> ""
            }
        } else {
            hasGarage = "false"
            garageType = "NA"
            garageQuality = "NA"
            garageCars = 0.toString()
            garageFinish = "NA"
        }

        val hasBasement: String
        val basementArea: String
        val basementExposure: String
        val basementRating: String
        val basementHeight: String
        val basementCondition: String
        if (btn_yes_basement_update.isChecked) {
            hasBasement = "true"
            basementArea = et_basement_area_update.text.toString().trim()

            basementExposure = when {
                basement_exposure_good_update.isChecked -> "good"
                basement_exposure_average_update.isChecked -> "average"
                basement_exposure_minimum_update.isChecked -> "minimum"
                basement_exposure_no_exposure_update.isChecked -> "no exposure"
                else -> ""
            }

            basementRating = when {
                rating_of_basement_good_update.isChecked -> "good"
                rating_of_basement_average_update.isChecked -> "average"
                rating_of_basement_below_average_update.isChecked -> "below average"
                rating_of_basement_rec_room_update.isChecked -> "average rec room"
                rating_of_basement_low_update.isChecked -> "low"
                rating_of_basement_unfinished_update.isChecked -> "unfinished"
                else -> ""
            }

            basementHeight = when {
                height_of_basement_excellent_update.isChecked -> "excellent"
                height_of_basement_good_update.isChecked -> "good"
                height_of_basement_average_update.isChecked -> "average"
                height_of_basement_fair_update.isChecked -> "fair"
                height_of_basement_poor_update.isChecked -> "poor"
                else -> ""
            }

            basementCondition = when {
                condition_of_basement_excellent_update.isChecked -> "excellent"
                condition_of_basement_good_update.isChecked -> "good"
                condition_of_basement_average_update.isChecked -> "average"
                condition_of_basement_fair_update.isChecked -> "fair"
                condition_of_basement_poor_update.isChecked -> "poor"
                condition_of_basement_not_available_update.isChecked -> "not available"
                else -> ""
            }
        } else {
            hasBasement = "false"
            basementArea = 0.toString()
            basementExposure = "NO"
            basementRating = "NO"
            basementHeight = "NO"
            basementCondition = "NO"
        }

        val hasFirePlace: String
        val firePlaceCount: String
        val firePlaceQuality: String
        if (btn_yes_fire_place_update.isChecked) {
            hasFirePlace = "true"
            firePlaceCount = count_fire_places_update.text.toString()
            firePlaceQuality = when {
                fire_place_quality_excellent_update.isChecked -> "excellent"
                fire_place_quality_good_update.isChecked -> "good"
                fire_place_quality_average_update.isChecked -> "average"
                fire_place_quality_fair_update.isChecked -> "fair"
                fire_place_quality_poor_update.isChecked -> "poor"
                else -> ""
            }
        } else {
            hasFirePlace = "false"
            firePlaceCount = 0.toString()
            firePlaceQuality = "NA"
        }

        val bedroomCount: String = count_bedrooms_update.text.toString()
        val bathroomCount: String = count_bathrooms_update.text.toString()
        val kitchenCount: String = count_kitchen_update.text.toString()

        val kitchenQuality = when {
            kitchen_quality_excellent_update.isChecked -> "excellent"
            kitchen_quality_good_update.isChecked -> "good"
            kitchen_quality_average_update.isChecked -> "average"
            kitchen_quality_fair_update.isChecked -> "fair"
            kitchen_quality_poor_update.isChecked -> "poor"
            else -> ""
        }

        val roomsWithoutBathroomCount: String = count_rooms_without_bathrooms_update.text.toString()

        val token = AppReferences.getToken(this@ThirdUpdateActivity)

        val residenceId = intent.getStringExtra("residenceId").toString()

        if (isValidInput()) {
            showProgressDialog(this@ThirdUpdateActivity, "Please Wait...")
            thirdUpdateViewModel.thirdUpdate(
                token, residenceId, hasGarage, garageType, garageQuality, garageCars,
                garageFinish, hasBasement, basementArea, basementExposure, basementRating,
                basementHeight, basementCondition, hasFirePlace, firePlaceCount, firePlaceQuality,
                bedroomCount, bathroomCount, kitchenCount, kitchenQuality, roomsWithoutBathroomCount
            )

            thirdUpdateViewModel.thirdUpdateLiveData.observe(this) { response ->
                hideProgressDialog()
                response.let {
                    val message = response.status
                    Log.e("Third Update", message)

                    val intent = Intent(
                        this@ThirdUpdateActivity, FourthUpdateActivity::class.java)
                    intent.putExtra("residenceId", residenceId)
                    Log.e("residenceId" , "Third Update is $residenceId")
                    startActivity(intent)
                }
            }

            thirdUpdateViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
//                        Toast.makeText(
//                            this@ThirdUpdateActivity, errorMessage, Toast.LENGTH_LONG
//                        ).show()

                        Log.e("ThirdUpdateActivity", "Third Update Error: $errorMessage")

                    } catch (e: JSONException) {
//                        Toast.makeText(
//                            this@ThirdUpdateActivity, error, Toast.LENGTH_LONG
//                        ).show()
                        Log.e("ThirdUpdateActivity", "Third Update Error: $error")
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        if (count_bedrooms_update.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the bedroom count", Toast.LENGTH_SHORT).show()
            return false
        }

        if (count_bathrooms_update.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the bathroom count", Toast.LENGTH_SHORT).show()
            return false
        }

        if (count_kitchen_update.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the kitchen count", Toast.LENGTH_SHORT).show()
            return false
        }

        if (count_rooms_without_bathrooms_update.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the rooms without bathroom count", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun initVisibility() {
        btn_yes_garage_update.setOnClickListener {
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
        }

        btn_no_garage_update.setOnClickListener {
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

        btn_yes_basement_update.setOnClickListener {
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

        btn_no_basement_update.setOnClickListener {
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

        btn_yes_fire_place_update.setOnClickListener {
            con_fire_places_update.visibility = View.VISIBLE
            tv_fire_place_quality_update.visibility = View.VISIBLE
            hsv_fire_place_quality_update.visibility = View.VISIBLE
        }

        btn_no_fire_place_update.setOnClickListener {
            con_fire_places_update.visibility = View.GONE
            tv_fire_place_quality_update.visibility = View.GONE
            hsv_fire_place_quality_update.visibility = View.GONE
        }
    }

    private fun initButtons() {
        plus_garage_cars_update.setOnClickListener {
            increaseCountGarageCars()
        }

        minus_garage_cars_update.setOnClickListener {
            decreaseCountGarageCars()
        }

        plus_fire_places_update.setOnClickListener {
            increaseCountFirePlace()
        }

        minus_fire_places_update.setOnClickListener {
            decreaseCountFirePlace()
        }

        plus_bedrooms_update.setOnClickListener {
            increaseCountBedRooms()
        }

        minus_bedrooms_update.setOnClickListener {
            decreaseCountBedRooms()
        }

        plus_bathrooms_update.setOnClickListener {
            increaseCountBathRooms()
        }

        minus_bathrooms_update.setOnClickListener {
            decreaseCountBathRooms()
        }

        plus_kitchen_update.setOnClickListener {
            increaseCountKitchen()
        }

        minus_kitchen_update.setOnClickListener {
            decreaseCountKitchen()
        }

        plus_rooms_without_bathrooms_update.setOnClickListener {
            increaseCountRoomsWithoutBathrooms()
        }

        minus_rooms_without_bathrooms_update.setOnClickListener {
            decreaseCountRoomsWithoutBathrooms()
        }
    }

    private fun increaseCountGarageCars() {
        val currentText = count_garage_cars_update.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_garage_cars_update.text = newNumber.toString()
        Log.e("Count Bedroom", newNumber.toString())
    }

    private fun decreaseCountGarageCars() {
        val currentText = count_garage_cars_update.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_garage_cars_update.text = newNumber.toString()
            Log.e("Count Garage Cars", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountFirePlace() {
        val currentText = count_fire_places_update.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_fire_places_update.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountFirePlace() {
        val currentText = count_fire_places_update.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_fire_places_update.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountBedRooms() {
        val currentText = count_bedrooms_update.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_bedrooms_update.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountBedRooms() {
        val currentText = count_bedrooms_update.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_bedrooms_update.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountBathRooms() {
        val currentText = count_bathrooms_update.text.toString()
        val currentNumber = currentText.toDouble()
        val newNumber = currentNumber + 0.5
        count_bathrooms_update.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountBathRooms() {
        val currentText = count_bathrooms_update.text.toString()
        val currentNumber = currentText.toDouble()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 0.5
            count_bathrooms_update.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountKitchen() {
        val currentText = count_kitchen_update.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_kitchen_update.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountKitchen() {
        val currentText = count_kitchen_update.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_kitchen_update.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountRoomsWithoutBathrooms() {
        val currentText = count_rooms_without_bathrooms_update.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_rooms_without_bathrooms_update.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountRoomsWithoutBathrooms() {
        val currentText = count_rooms_without_bathrooms_update.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_rooms_without_bathrooms_update.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initChips() {
        val chipIds = listOf(
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