package com.example.finalproject.ui.update_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.update_listing.models.GetResidenceResponse
import retrofit2.Call

class GetResidenceRepository(
    private val apiService: ApiService
) {
    fun getResidence(
        token: String,
        residenceId: String
    ): Call<GetResidenceResponse> {
        return apiService.getResidence(token, residenceId)
    }
}