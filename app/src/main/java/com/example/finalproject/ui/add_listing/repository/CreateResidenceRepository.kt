package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.CreateResidenceResponse
import com.example.finalproject.ui.add_listing.request.CreateResidenceRequest
import retrofit2.Call

class CreateResidenceRepository(
    private val apiService: ApiService
) {
    fun createResidence(
        token: String,
        title: String,
        type: String,
        category: String
    ): Call<CreateResidenceResponse> {
        val data = CreateResidenceRequest(title, type, category)
        return apiService.createResidence(token, data)
    }
}