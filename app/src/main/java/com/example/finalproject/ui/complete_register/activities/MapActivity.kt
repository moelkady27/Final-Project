package com.example.finalproject.ui.complete_register.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.complete_register.viewModels.SetLocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_map.btnCurrentLocation
import kotlinx.android.synthetic.main.activity_map.btnSaveLocation
import org.json.JSONException
import org.json.JSONObject

class MapActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var networkUtils: NetworkUtils

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 101

    private var currentLocation: Location? = null
    private var selectedLocation: LatLng? = null

    private var locationSelected: Boolean = false

    private lateinit var setLocationViewModel: SetLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        networkUtils = NetworkUtils(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setLocationViewModel = ViewModelProvider(this).get(SetLocationViewModel::class.java)

        btnCurrentLocation.setOnClickListener {
            fetchCurrentLocation()
            locationSelected = false
        }

        btnSaveLocation.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                if (locationSelected) {
                    onSelectedLocation()
                } else {
                    onCurrentLocation()
                }
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val supportMapFragment =
                    (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this@MapActivity)
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Unable to fetch the location",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun fetchCurrentLocation() {
        if (currentLocation != null) {
            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            val markerOptions = MarkerOptions().position(latLng).title("I Am Here!")

            (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync { googleMap ->
                googleMap.clear()
                googleMap.addMarker(markerOptions)

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
    }

    private fun onSelectedLocation() {
        if (selectedLocation != null) {
            val token = AppReferences.getToken(this@MapActivity)

            showProgressDialog(this@MapActivity , "Setting up your LocationSignIn")

            setLocationViewModel.setLocation(token, selectedLocation!!.longitude, selectedLocation!!.latitude)
            setLocationViewModel.locationResponseLiveData.observe(this, Observer { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status

                    Log.e("MapActivity", "Status: $status")

                    Toast.makeText(this@MapActivity, status, Toast.LENGTH_LONG).show()


                    val intent = Intent()
                    intent.putExtra("latitude", selectedLocation!!.latitude)
                    intent.putExtra("longitude", selectedLocation!!.longitude)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            })

            setLocationViewModel.errorLiveData.observe(this, Observer { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@MapActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("MapActivity", "Map Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(this@MapActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })

        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please select a location on the map",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun onCurrentLocation() {
        if (currentLocation != null) {
            val token = AppReferences.getToken(this@MapActivity)

            showProgressDialog(this@MapActivity , "Setting up your LocationSignIn")

            setLocationViewModel.setLocation(token, currentLocation!!.longitude, currentLocation!!.latitude)
            setLocationViewModel.locationResponseLiveData.observe(this, Observer { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status

                    Log.e("MapActivity", "Status: $status")

                    Toast.makeText(this@MapActivity, status, Toast.LENGTH_LONG).show()

                    val intent = Intent()
                    intent.putExtra("latitude", currentLocation!!.latitude)
                    intent.putExtra("longitude", currentLocation!!.longitude)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            })

            setLocationViewModel.errorLiveData.observe(this, Observer { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@MapActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("MapActivity", "Map Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(this@MapActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })

        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please select a location on the map",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (selectedLocation != null) {
            val latLng = LatLng(selectedLocation!!.latitude, selectedLocation!!.longitude)
            val markerOptions = MarkerOptions().position(latLng).title("Selected LocationSignIn")
            googleMap.clear()
            googleMap.addMarker(markerOptions)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }

        googleMap.setOnMapClickListener { newLatLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(newLatLng).title("Selected LocationSignIn"))
            selectedLocation = newLatLng
            locationSelected = true
        }

        googleMap.uiSettings.isZoomControlsEnabled = true

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "LocationSignIn permission denied. Cannot show current location.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}