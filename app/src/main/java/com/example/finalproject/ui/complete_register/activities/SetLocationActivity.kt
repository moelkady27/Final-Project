package com.example.finalproject.ui.complete_register.activities

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.BaseActivity
import kotlinx.android.synthetic.main.activity_set_location.btn_next_set_location
import kotlinx.android.synthetic.main.activity_set_location.btn_set_location
import kotlinx.android.synthetic.main.activity_set_location.toolbar_set_location
import kotlinx.android.synthetic.main.activity_set_location.tv_set_location_title_3
import java.io.IOException

class SetLocationActivity : BaseActivity(){

    private lateinit var networkUtils: NetworkUtils

    private val REQUEST_CODE_MAP = 102

    private var isLocationSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_location)

        networkUtils = NetworkUtils(this)

        btn_set_location.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                val mapIntent = Intent(this@SetLocationActivity, MapActivity::class.java)
                startActivityForResult(mapIntent, REQUEST_CODE_MAP)
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

        btn_next_set_location.setOnClickListener {
            if (isLocationSelected) {
                startActivity(Intent(this@SetLocationActivity , CongratsActivity::class.java))
            } else {
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show()
            }
        }

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_set_location)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_set_location.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK && data != null) {

            isLocationSelected = true

            val latitude = data.getDoubleExtra("latitude", 0.0)
            val longitude = data.getDoubleExtra("longitude", 0.0)

            showCurrentLocationAsText(latitude, longitude)

            Log.e("Selected LocationSignIn:" , "$latitude, $longitude")
        }
    }

    private fun showCurrentLocationAsText(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this)

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val locationName = address.getAddressLine(0)
                tv_set_location_title_3.text = locationName
            }

        } catch (e: IOException) {
            e.printStackTrace()
            tv_set_location_title_3.text = "Current LocationSignIn: $latitude, $longitude"
        }
    }
}