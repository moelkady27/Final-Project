package com.example.finalproject.ui.residence_details.activities

import android.os.Bundle
import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places

class MapResidenceActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var networkUtils: NetworkUtils

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var mGoogleMap: GoogleMap?= null

    private var residenceLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_residence)

        networkUtils = NetworkUtils(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        residenceLocation = LatLng(latitude, longitude)

        Log.e("latitude", latitude.toString())
        Log.e("longitude", longitude.toString())
        initView()
    }

    private fun initView() {

        Places.initialize(applicationContext, getString(R.string.apiKey))

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapResidence) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        residenceLocation?.let {
            val markerOptions = MarkerOptions().position(it).title("Residence Location")
            mGoogleMap?.addMarker(markerOptions)
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))

//            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(42.0307812, -93.63191309999999), 13.0f))
        }

        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
    }
}