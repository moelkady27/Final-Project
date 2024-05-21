package com.example.finalproject.ui.profile.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.profile.models.ResidenceResponse
import retrofit2.Call

class PendingRepository(
    private val apiService: ApiService
) {
    fun getPending(
        token: String,
        page: Int
    ):Call<ResidenceResponse> {
        return apiService.getPendingResidence(token, page)
    }
}