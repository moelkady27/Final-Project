package com.example.finalproject.ui.profile.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.profile.models.ResidenceResponse
import retrofit2.Call

class ApprovedRepository(
    private val apiService: ApiService
) {
    fun getApproved(
        token: String,
        page: Int
    ):Call<ResidenceResponse> {
        return apiService.getApprovedResidence(token, page)
    }
}