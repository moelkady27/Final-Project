package com.example.finalproject.ui.profile.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.profile.models.ResidenceResponse
import retrofit2.Call

class SoldRepository(
    private val apiService: ApiService
) {
    fun getSold(
        token: String
    ):Call<ResidenceResponse> {
        return apiService.getSoldResidence(token)
    }
}