package com.example.finalproject.ui.add_listing.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.factory.LocationResidenceFactory
import com.example.finalproject.ui.add_listing.repository.SetLocationResidenceRepository
import com.example.finalproject.ui.add_listing.viewModel.SetLocationResidenceViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_listing_map.btn_add_listing_confirm_location
import org.json.JSONException
import org.json.JSONObject

class AddListingMapActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var networkUtils: NetworkUtils

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private val REQUEST_CODE = 101

    private var selectedLocation: LatLng? = null

    private var locationSelected: Boolean = false

    private lateinit var setLocationResidenceViewModel: SetLocationResidenceViewModel

    private var mGoogleMap: GoogleMap?= null

    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_map)

        networkUtils = NetworkUtils(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initView()
    }

    private fun initView() {
        val setLocationResidenceRepository = SetLocationResidenceRepository(RetrofitClient.instance)
        val factory = LocationResidenceFactory(setLocationResidenceRepository)
        setLocationResidenceViewModel = ViewModelProvider(this, factory
        )[SetLocationResidenceViewModel::class.java]

        fetchLocation()

        //        search

        Places.initialize(applicationContext, getString(R.string.apiKey))

        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.add_listing_search)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID , Place.Field.ADDRESS , Place.Field.LAT_LNG))
        autocompleteFragment.setHint("Search for location")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Toast.makeText(this@AddListingMapActivity , "There is Error on Search" , Toast.LENGTH_LONG).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                addMarkerToMap(latLng, place.address ?: "")
                zoomOnMap(latLng!!)

                selectedLocation = latLng
                locationSelected = true
            }
        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

//        select location

        btn_add_listing_confirm_location.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                if (locationSelected) {
                    onSelectedLocation()
                }
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

    }

    private fun zoomOnMap(latlng: LatLng) {
        val newLatLangZoom = CameraUpdateFactory.newLatLngZoom(latlng , 12f)
        mGoogleMap?.animateCamera(newLatLangZoom)
    }

    private fun addMarkerToMap(latLng: LatLng?, address: String) {
        latLng?.let {
            mGoogleMap?.clear()
            mGoogleMap?.addMarker(MarkerOptions().position(it).title(address))

            Log.e("address", address)

            val intent = Intent()
            intent.putExtra("address", address)
            setResult(RESULT_OK, intent)
        }
    }

    //    getting location by permissions
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
                val supportMapFragment =
                    (supportFragmentManager.findFragmentById(R.id.add_listing_map) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this@AddListingMapActivity)
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Unable to fetch the location",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun onSelectedLocation() {
        if (selectedLocation != null) {
            val token = AppReferences.getToken(this@AddListingMapActivity)

            showProgressDialog(this@AddListingMapActivity , "Setting up your LocationSignIn")

            setLocationResidenceViewModel.setLocationResidence(token, "66408b0c1799d8e78a3e2b91", selectedLocation!!.longitude, selectedLocation!!.latitude)
            setLocationResidenceViewModel.locationResponseLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status

                    Log.e("MapActivity", "Status: $status")

                    Toast.makeText(this@AddListingMapActivity, status, Toast.LENGTH_LONG).show()

                    startActivity(Intent(this, AddListingPhotosActivity::class.java))
                }
            }

            setLocationResidenceViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@AddListingMapActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("MapActivity", "Map Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(this@AddListingMapActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please select a location on the map",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
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