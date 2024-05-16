package com.example.finalproject.ui.add_listing.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_third_complete.basement_exposure_scroll
import kotlinx.android.synthetic.main.activity_third_complete.btn_no_basement
import kotlinx.android.synthetic.main.activity_third_complete.btn_no_fire_place
import kotlinx.android.synthetic.main.activity_third_complete.btn_no_garage
import kotlinx.android.synthetic.main.activity_third_complete.btn_yes_basement
import kotlinx.android.synthetic.main.activity_third_complete.btn_yes_fire_place
import kotlinx.android.synthetic.main.activity_third_complete.btn_yes_garage
import kotlinx.android.synthetic.main.activity_third_complete.con_fire_places
import kotlinx.android.synthetic.main.activity_third_complete.con_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.condition_of_basement_scroll
import kotlinx.android.synthetic.main.activity_third_complete.count_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.count_bedrooms
import kotlinx.android.synthetic.main.activity_third_complete.count_fire_places
import kotlinx.android.synthetic.main.activity_third_complete.count_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.count_kitchen
import kotlinx.android.synthetic.main.activity_third_complete.count_rooms_without_bathrooms
import kotlinx.android.synthetic.main.activity_third_complete.et_basement_area
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality
import kotlinx.android.synthetic.main.activity_third_complete.fire_place_quality_scroll
import kotlinx.android.synthetic.main.activity_third_complete.garage_finish_scroll
import kotlinx.android.synthetic.main.activity_third_complete.garage_quality_scroll
import kotlinx.android.synthetic.main.activity_third_complete.garage_type
import kotlinx.android.synthetic.main.activity_third_complete.garage_type_scroll
import kotlinx.android.synthetic.main.activity_third_complete.height_of_basement_scroll
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
import kotlinx.android.synthetic.main.activity_third_complete.rating_of_basement_scroll
import kotlinx.android.synthetic.main.activity_third_complete.toolbar_third_complete
import kotlinx.android.synthetic.main.activity_third_complete.tv_basement_area
import kotlinx.android.synthetic.main.activity_third_complete.tv_basement_exposure
import kotlinx.android.synthetic.main.activity_third_complete.tv_condition_of_basement
import kotlinx.android.synthetic.main.activity_third_complete.tv_garage_cars
import kotlinx.android.synthetic.main.activity_third_complete.tv_garage_finish
import kotlinx.android.synthetic.main.activity_third_complete.tv_garage_quality
import kotlinx.android.synthetic.main.activity_third_complete.tv_height_of_basement
import kotlinx.android.synthetic.main.activity_third_complete.tv_rating_of_basement

class ThirdCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_complete)

        val residenceId = intent.getStringExtra("residenceId").toString()
        Log.e("residenceId" , "third is $residenceId")

        initView()

        setupActionBar()
    }

    private fun initView() {

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

}