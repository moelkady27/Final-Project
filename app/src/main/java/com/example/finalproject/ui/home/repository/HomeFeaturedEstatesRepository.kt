package com.example.finalproject.ui.home.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.home.models.GetAllResidencesResponse
import retrofit2.Call

class HomeFeaturedEstatesRepository(
    private val apiService: ApiService
) {
    fun getFeaturedEstates(
        token: String,
        page: Int
    ): Call<GetAllResidencesResponse> {
        return apiService.getFeaturedEstates(token, page)
    }
}
