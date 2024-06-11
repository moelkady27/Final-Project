package com.example.finalproject.ui.home.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.home.models.GetNearestResidencesResponse
import retrofit2.Call

class HomePopularEstatesRepository(
    private val apiService: ApiService
) {
    fun getNearestEstates(
        token: String,
        page: Int
    ): Call<GetNearestResidencesResponse> {
        return apiService.getNearestEstates(token, page)
    }
}
