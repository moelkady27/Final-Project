package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.SetLocationResidenceResponse
import com.example.finalproject.ui.complete_register.models.LocationResponse
import retrofit2.Call

class SetLocationResidenceRepository(
    private val apiService: ApiService
) {
    fun setLocationRe(
        token: String,
        residenceId: String,
        longitude: Double,
        latitude: Double
    ): Call<SetLocationResidenceResponse> {
        return apiService.locationResidence(token, residenceId, longitude, latitude)
    }
}