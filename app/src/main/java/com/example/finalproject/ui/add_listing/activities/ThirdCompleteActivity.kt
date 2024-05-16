package com.example.finalproject.ui.add_listing.activities

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
import com.example.finalproject.ui.add_listing.factory.ThirdCompleteFactory
import com.example.finalproject.ui.add_listing.repository.ThirdCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.ThirdCompleteViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_third_complete.basement_exposure_average
import kotlinx.android.synthetic.main.activity_third_complete.basement_exposure_good
import kotlinx.android.synthetic.main.activity_third_complete.basement_exposure_minimum
import kotlinx.android.synthetic.main.activity_third_complete.basement_exposure_no_exposure
import kotlinx.android.synthetic.main.activity_third_complete.basement_exposure_scroll
import kotlinx.android.synthetic.main.activity_third_complete.btn_next_third_complete
import kotlinx.android.synthetic.main.activity_third_complete.btn_no_basement
import kotlinx.android.synthetic.main.activity_third_complete.btn_no_fire_place
import kotlinx.android.synthetic.main.activity_third_complete.btn_no_garage
import kotlinx.android.synthetic.main.activity_third_complete.btn_yes_basement
import kotlinx.android.synthetic.main.activity_third_complete.btn_yes_fire_place
import kotlinx.android.synthetic.main.activity_third_complete.btn_yes_garage
import kotlinx.android.synthetic.main.activity_third_complete.con_fire_places
import kotlinx.android.synthetic.main.activity_third_complete.con_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_average
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_excellent
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_fair
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_good
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_not_available
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_poor
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_scroll
import kotlinx.android.synthetic.main.activity_third_complete.count_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.count_bedrooms
import kotlinx.android.synthetic.main.activity_third_complete.count_fire_places
import kotlinx.android.synthetic.main.activity_third_complete.count_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.count_kitchen
import kotlinx.android.synthetic.main.activity_third_complete.count_rooms_without_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.et_basement_area
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality_average
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality_excellent
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality_fair
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality_good
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality_poor
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality_scroll
import kotlinx.android.synthetic.main.activity_third_complete.garage_finish_finished
import kotlinx.android.synthetic.main.activity_third_complete.garage_finish_not_available
import kotlinx.android.synthetic.main.activity_third_complete.garage_finish_rough_finished
import kotlinx.android.synthetic.main.activity_third_complete.garage_finish_scroll
import kotlinx.android.synthetic.main.activity_third_complete.garage_finish_unfinished
import kotlinx.android.synthetic.main.activity_third_complete.garage_quality_average
import kotlinx.android.synthetic.main.activity_third_complete.garage_quality_excellent
import kotlinx.android.synthetic.main.activity_third_complete.garage_quality_fair
import kotlinx.android.synthetic.main.activity_third_complete.garage_quality_good
import kotlinx.android.synthetic.main.activity_third_complete.garage_quality_poor
import kotlinx.android.synthetic.main.activity_third_complete.garage_quality_scroll
import kotlinx.android.synthetic.main.activity_third_complete.garage_type
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_attached
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_basement
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_built_in
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_car_port
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_detached
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_more_than_one
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_not_available
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_scroll
import kotlinx.android.synthetic.main.activity_third_complete.height_of_basement_average
import kotlinx.android.synthetic.main.activity_third_complete.height_of_basement_excellent
import kotlinx.android.synthetic.main.activity_third_complete.height_of_basement_fair
import kotlinx.android.synthetic.main.activity_third_complete.height_of_basement_good
import kotlinx.android.synthetic.main.activity_third_complete.height_of_basement_poor
import kotlinx.android.synthetic.main.activity_third_complete.height_of_basement_scroll
import kotlinx.android.synthetic.main.activity_third_complete.kitchen_quality_average
import kotlinx.android.synthetic.main.activity_third_complete.kitchen_quality_excellent
import kotlinx.android.synthetic.main.activity_third_complete.kitchen_quality_fair
import kotlinx.android.synthetic.main.activity_third_complete.kitchen_quality_good
import kotlinx.android.synthetic.main.activity_third_complete.kitchen_quality_poor
import kotlinx.android.synthetic.main.activity_third_complete.minus_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.minus_bedrooms
import kotlinx.android.synthetic.main.activity_third_complete.minus_fire_places
import kotlinx.android.synthetic.main.activity_third_complete.minus_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.minus_kitchen
import kotlinx.android.synthetic.main.activity_third_complete.minus_rooms_without_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.plus_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.plus_bedrooms
import kotlinx.android.synthetic.main.activity_third_complete.plus_fire_places
import kotlinx.android.synthetic.main.activity_third_complete.plus_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.plus_kitchen
import kotlinx.android.synthetic.main.activity_third_complete.plus_rooms_without_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_average
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_below_average
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_good
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_low
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_rec_room
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_scroll
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_unfinished
import kotlinx.android.synthetic.main.activity_third_complete.toolbar_third_complete
import kotlinx.android.synthetic.main.activity_third_complete.tv_basement_area
import kotlinx.android.synthetic.main.activity_third_complete.tv_basement_exposure
import kotlinx.android.synthetic.main.activity_third_complete.tv_condition_of_basement
import kotlinx.android.synthetic.main.activity_third_complete.tv_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.tv_garage_finish
import kotlinx.android.synthetic.main.activity_third_complete.tv_garage_quality
import kotlinx.android.synthetic.main.activity_third_complete.tv_height_of_basement
import kotlinx.android.synthetic.main.activity_third_complete.tv_rating_of_basement
import org.json.JSONException
import org.json.JSONObject

class ThirdCompleteActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var thirdCompleteViewModel: ThirdCompleteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_complete)

        val residenceId = intent.getStringExtra("residenceId").toString()
        Log.e("residenceId" , "third is $residenceId")

        networkUtils = NetworkUtils(this@ThirdCompleteActivity)

        initView()

        initChips()

        setupActionBar()
    }

    private fun initView() {
        val thirdCompleteRepository = ThirdCompleteRepository(RetrofitClient.instance)
        val factory = ThirdCompleteFactory(thirdCompleteRepository)
        thirdCompleteViewModel = ViewModelProvider(this, factory
        )[ThirdCompleteViewModel::class.java]

        garage_type.visibility = View.GONE
        garage_type_scroll.visibility = View.GONE
        tv_garage_quality.visibility = View.GONE
        garage_quality_scroll.visibility = View.GONE
        tv_garage_cars.visibility = View.GONE
        con_garage_cars.visibility = View.GONE
        tv_garage_finish.visibility = View.GONE
        garage_finish_scroll.visibility = View.GONE

        btn_yes_garage.setOnClickListener {
            garage_type.visibility = View.VISIBLE
            garage_type_scroll.visibility = View.VISIBLE
            tv_garage_quality.visibility = View.VISIBLE
            garage_quality_scroll.visibility = View.VISIBLE
            tv_garage_cars.visibility = View.VISIBLE
            con_garage_cars.visibility = View.VISIBLE
            tv_garage_finish.visibility = View.VISIBLE
            garage_finish_scroll.visibility = View.VISIBLE
        }

        btn_no_garage.setOnClickListener {
            garage_type.visibility = View.GONE
            garage_type_scroll.visibility = View.GONE
            tv_garage_quality.visibility = View.GONE
            garage_quality_scroll.visibility = View.GONE
            tv_garage_cars.visibility = View.GONE
            con_garage_cars.visibility = View.GONE
            tv_garage_finish.visibility = View.GONE
            garage_finish_scroll.visibility = View.GONE
        }

        tv_basement_area.visibility = View.GONE
        et_basement_area.visibility = View.GONE
        tv_basement_exposure.visibility = View.GONE
        basement_exposure_scroll.visibility = View.GONE
        tv_rating_of_basement.visibility = View.GONE
        rating_of_basement_scroll.visibility = View.GONE
        tv_height_of_basement.visibility = View.GONE
        height_of_basement_scroll.visibility = View.GONE
        tv_condition_of_basement.visibility = View.GONE
        condition_of_basement_scroll.visibility = View.GONE

        btn_yes_basement.setOnClickListener {
            tv_basement_area.visibility = View.VISIBLE
            et_basement_area.visibility = View.VISIBLE
            tv_basement_exposure.visibility = View.VISIBLE
            basement_exposure_scroll.visibility = View.VISIBLE
            tv_rating_of_basement.visibility = View.VISIBLE
            rating_of_basement_scroll.visibility = View.VISIBLE
            tv_height_of_basement.visibility = View.VISIBLE
            height_of_basement_scroll.visibility = View.VISIBLE
            tv_condition_of_basement.visibility = View.VISIBLE
            condition_of_basement_scroll.visibility = View.VISIBLE
        }

        btn_no_basement.setOnClickListener {
            tv_basement_area.visibility = View.GONE
            et_basement_area.visibility = View.GONE
            tv_basement_exposure.visibility = View.GONE
            basement_exposure_scroll.visibility = View.GONE
            tv_rating_of_basement.visibility = View.GONE
            rating_of_basement_scroll.visibility = View.GONE
            tv_height_of_basement.visibility = View.GONE
            height_of_basement_scroll.visibility = View.GONE
            tv_condition_of_basement.visibility = View.GONE
            condition_of_basement_scroll.visibility = View.GONE
        }

        con_fire_places.visibility = View.GONE
        fire_place_quality.visibility = View.GONE
        fire_place_quality_scroll.visibility = View.GONE

        btn_yes_fire_place.setOnClickListener {
            con_fire_places.visibility = View.VISIBLE
            fire_place_quality.visibility = View.VISIBLE
            fire_place_quality_scroll.visibility = View.VISIBLE
        }

        btn_no_fire_place.setOnClickListener {
            con_fire_places.visibility = View.GONE
            fire_place_quality.visibility = View.GONE
            fire_place_quality_scroll.visibility = View.GONE
        }

        plus_garage_cars.setOnClickListener {
            increaseCountGarageCars()
        }

        minus_garage_cars.setOnClickListener {
            decreaseCountGarageCars()
        }

        plus_fire_places.setOnClickListener {
            increaseCountFirePlace()
        }

        minus_fire_places.setOnClickListener {
            decreaseCountFirePlace()
        }

        plus_bedrooms.setOnClickListener {
            increaseCountBedRooms()
        }

        minus_bedrooms.setOnClickListener {
            decreaseCountBedRooms()
        }

        plus_bathrooms.setOnClickListener {
            increaseCountBathRooms()
        }

        minus_bathrooms.setOnClickListener {
            decreaseCountBathRooms()
        }

        plus_kitchen.setOnClickListener {
            increaseCountKitchen()
        }

        minus_kitchen.setOnClickListener {
            decreaseCountKitchen()
        }

        plus_rooms_without_bathrooms.setOnClickListener {
            increaseCountRoomsWithoutBathrooms()
        }

        minus_rooms_without_bathrooms.setOnClickListener {
            decreaseCountRoomsWithoutBathrooms()
        }

        btn_next_third_complete.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                thirdComplete()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun thirdComplete() {
        val hasGarage: String
        val garageType: String
        val garageQuality: String
        val garageCars: String
        val garageFinish: String

        if (btn_yes_garage.isChecked) {
            hasGarage = "true"
            garageType = when {
                garage_type_more_than_one.isChecked -> "more than one type"
                garage_type_attached.isChecked -> "attached"
                garage_type_basement.isChecked -> "basement"
                garage_type_built_in.isChecked -> "built-In"
                garage_type_car_port.isChecked -> "car Port"
                garage_type_detached.isChecked -> "detached"
                garage_type_not_available.isChecked -> "not available"
                else -> ""
            }
            garageQuality = when {
                garage_quality_excellent.isChecked -> "excellent"
                garage_quality_good.isChecked -> "good"
                garage_quality_average.isChecked -> "average"
                garage_quality_fair.isChecked -> "fair"
                garage_quality_poor.isChecked -> "poor"
                else -> ""
            }

            garageCars = count_garage_cars.text.toString().trim()

            garageFinish = when {
                garage_finish_finished.isChecked -> "finished"
                garage_finish_rough_finished.isChecked -> "rough finished"
                garage_finish_unfinished.isChecked -> "unfinished"
                garage_finish_not_available.isChecked -> "not available"
                else -> ""
            }
        } else {
            hasGarage = "false"
            garageType = ""
            garageQuality = ""
            garageCars = ""
            garageFinish = ""
        }

        val hasBasement: String
        val basementArea: String
        val basementExposure: String
        val basementRating: String
        val basementHeight: String
        val basementCondition: String
        if (btn_yes_basement.isChecked) {
            hasBasement = "true"
            basementArea = et_basement_area.text.toString().trim()

            basementExposure = when {
                basement_exposure_good.isChecked -> "good"
                basement_exposure_average.isChecked -> "average"
                basement_exposure_minimum.isChecked -> "minimum"
                basement_exposure_no_exposure.isChecked -> "no exposure"
                else -> ""
            }

            basementRating = when {
                rating_of_basement_good.isChecked -> "good"
                rating_of_basement_average.isChecked -> "average"
                rating_of_basement_below_average.isChecked -> "below average"
                rating_of_basement_rec_room.isChecked -> "average rec room"
                rating_of_basement_low.isChecked -> "low"
                rating_of_basement_unfinished.isChecked -> "unfinished"
                else -> ""
            }

            basementHeight = when {
                height_of_basement_excellent.isChecked -> "excellent"
                height_of_basement_good.isChecked -> "good"
                height_of_basement_average.isChecked -> "average"
                height_of_basement_fair.isChecked -> "fair"
                height_of_basement_poor.isChecked -> "poor"
                else -> ""
            }

            basementCondition = when {
                condition_of_basement_excellent.isChecked -> "excellent"
                condition_of_basement_good.isChecked -> "good"
                condition_of_basement_average.isChecked -> "average"
                condition_of_basement_fair.isChecked -> "fair"
                condition_of_basement_poor.isChecked -> "poor"
                condition_of_basement_not_available.isChecked -> "not available"
                else -> ""
            }
        } else {
            hasBasement = "false"
            basementArea = ""
            basementExposure = ""
            basementRating = ""
            basementHeight = ""
            basementCondition = ""
        }

        val hasFirePlace: String
        val firePlaceCount: String
        val firePlaceQuality: String
        if (btn_yes_fire_place.isChecked) {
            hasFirePlace = "true"
            firePlaceCount = count_fire_places.text.toString()
            firePlaceQuality = when {
                fire_place_quality_excellent.isChecked -> "excellent"
                fire_place_quality_good.isChecked -> "good"
                fire_place_quality_average.isChecked -> "average"
                fire_place_quality_fair.isChecked -> "fair"
                fire_place_quality_poor.isChecked -> "poor"
                else -> ""
            }
        } else {
            hasFirePlace = "false"
            firePlaceCount = ""
            firePlaceQuality = ""
        }

        val bedroomCount: String = count_bedrooms.text.toString()
        val bathroomCount: String = count_bathrooms.text.toString()
        val kitchenCount: String = count_kitchen.text.toString()

        val kitchenQuality = when {
            kitchen_quality_excellent.isChecked -> "excellent"
            kitchen_quality_good.isChecked -> "good"
            kitchen_quality_average.isChecked -> "average"
            kitchen_quality_fair.isChecked -> "fair"
            kitchen_quality_poor.isChecked -> "poor"
            else -> ""
        }

        val roomsWithoutBathroomCount: String = count_rooms_without_bathrooms.text.toString()

        val token = AppReferences.getToken(this@ThirdCompleteActivity)

        val residenceId = intent.getStringExtra("residenceId").toString()

        if (isValidInput()) {
            showProgressDialog(this@ThirdCompleteActivity, "Please Wait...")
            thirdCompleteViewModel.thirdComplete(
                token, residenceId, hasGarage, garageType, garageQuality, garageCars,
                garageFinish, hasBasement, basementArea, basementExposure, basementRating,
                basementHeight, basementCondition, hasFirePlace, firePlaceCount, firePlaceQuality,
                bedroomCount, bathroomCount, kitchenCount, kitchenQuality, roomsWithoutBathroomCount
            )

            thirdCompleteViewModel.thirdCompleteLiveData.observe(this) { response ->
                hideProgressDialog()
                response.let {
                    val message = response.status

                    Log.e("Third Complete", message)
                }
            }

            thirdCompleteViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@ThirdCompleteActivity, errorMessage, Toast.LENGTH_LONG
                        ).show()

                        Log.e("ThirdCompleteActivity", "Third Complete Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@ThirdCompleteActivity, error, Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        if (count_bedrooms.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the bedroom count", Toast.LENGTH_SHORT).show()
            return false
        }

        if (count_bathrooms.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the bathroom count", Toast.LENGTH_SHORT).show()
            return false
        }

        if (count_kitchen.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the kitchen count", Toast.LENGTH_SHORT).show()
            return false
        }

        if (count_rooms_without_bathrooms.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter the rooms without bathroom count", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_third_complete)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_third_complete.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun increaseCountGarageCars() {
        val currentText = count_garage_cars.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_garage_cars.text = newNumber.toString()
        Log.e("Count Bedroom", newNumber.toString())
    }

    private fun decreaseCountGarageCars() {
        val currentText = count_garage_cars.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_garage_cars.text = newNumber.toString()
            Log.e("Count Garage Cars", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountFirePlace() {
        val currentText = count_fire_places.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_fire_places.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountFirePlace() {
        val currentText = count_fire_places.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_fire_places.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountBedRooms() {
        val currentText = count_bedrooms.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_bedrooms.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountBedRooms() {
        val currentText = count_bedrooms.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_bedrooms.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountBathRooms() {
        val currentText = count_bathrooms.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_bathrooms.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountBathRooms() {
        val currentText = count_bathrooms.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_bathrooms.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountKitchen() {
        val currentText = count_kitchen.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_kitchen.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountKitchen() {
        val currentText = count_kitchen.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_kitchen.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseCountRoomsWithoutBathrooms() {
        val currentText = count_rooms_without_bathrooms.text.toString()
        val currentNumber = currentText.toInt()
        val newNumber = currentNumber + 1
        count_rooms_without_bathrooms.text = newNumber.toString()
        Log.e("Count Fire Place", newNumber.toString())
    }

    private fun decreaseCountRoomsWithoutBathrooms() {
        val currentText = count_rooms_without_bathrooms.text.toString()
        val currentNumber = currentText.toInt()
        if (currentNumber > 0) {
            val newNumber = currentNumber - 1
            count_rooms_without_bathrooms.text = newNumber.toString()
            Log.e("Count Fire Place", newNumber.toString())
        } else {
            Toast.makeText(this, "Cannot decrease below 0", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initChips() {
        val chipId = listOf(
            R.id.garage_type_more_than_one, R.id.garage_type_attached, R.id.garage_type_basement,
            R.id.garage_type_built_in, R.id.garage_type_car_port, R.id.garage_type_detached, R.id.garage_type_not_available,

            R.id.garage_quality_excellent, R.id.garage_quality_good, R.id.garage_quality_average,
            R.id.garage_quality_fair, R.id.garage_quality_poor,

            R.id.garage_finish_finished, R.id.garage_finish_rough_finished,
            R.id.garage_finish_unfinished, R.id.garage_finish_not_available,

            R.id.basement_exposure_good, R.id.basement_exposure_average,
            R.id.basement_exposure_minimum, R.id.basement_exposure_no_exposure,

            R.id.rating_of_basement_good, R.id.rating_of_basement_average, R.id.rating_of_basement_below_average,
            R.id.rating_of_basement_rec_room, R.id.rating_of_basement_low, R.id.rating_of_basement_unfinished,

            R.id.height_of_basement_excellent, R.id.height_of_basement_good, R.id.height_of_basement_average,
            R.id.height_of_basement_fair, R.id.height_of_basement_poor,

            R.id.condition_of_basement_excellent, R.id.condition_of_basement_good, R.id.condition_of_basement_average,
            R.id.condition_of_basement_fair, R.id.condition_of_basement_poor, R.id.condition_of_basement_not_available,

            R.id.fire_place_quality_excellent, R.id.fire_place_quality_good, R.id.fire_place_quality_average,
            R.id.fire_place_quality_fair, R.id.fire_place_quality_poor,

            R.id.kitchen_quality_excellent, R.id.kitchen_quality_good, R.id.kitchen_quality_average,
            R.id.kitchen_quality_fair, R.id.kitchen_quality_poor,
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

}