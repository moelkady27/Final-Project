package com.example.finalproject.ui.complete_register.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.complete_register.models.LocationResponse
import retrofit2.Call

class SetLocationRepository(
    private val apiService: ApiService
) {
    fun setLocation(
        token: String,
        longitude: Double,
        latitude: Double
    ): Call<LocationResponse> {
        return apiService.location(token, longitude, latitude)
    }
}