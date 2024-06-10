package com.example.finalproject.ui.update_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.request.UpdateResidenceRequest
import retrofit2.Call

class UpdateResidenceRepository(
    private val apiService: ApiService
) {
    fun updateResidence(
        token: String,
        residenceId: String,
        title: String,
        type: String,
        category: String,
    ): Call<UpdateResidenceResponse>{
        val data = UpdateResidenceRequest(title, type, category)
        return apiService.updateResidence(token, residenceId, data)
    }
}