package com.example.finalproject.ui.profile.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.profile.models.PendingResponse
import retrofit2.Call

class PendingRepository(
    private val apiService: ApiService
) {
    fun getPending(
        token: String
    ):Call<PendingResponse> {
        return apiService.getPendingResidence(token)
    }
}