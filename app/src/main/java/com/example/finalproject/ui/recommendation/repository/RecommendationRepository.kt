package com.example.finalproject.ui.recommendation.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.recommendation.models.GetRecommendedEstatesResponse
import retrofit2.Call

class RecommendationRepository(
    private val apiService: ApiService
) {

    fun getRecommendation(
        token: String,
        restaurantId: String
    ): Call<GetRecommendedEstatesResponse>{
        return apiService.getRecommendedEstates(token, restaurantId)
    }
}