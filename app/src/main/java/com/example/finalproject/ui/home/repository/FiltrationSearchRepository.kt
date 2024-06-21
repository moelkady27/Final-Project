package com.example.finalproject.ui.home.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.home.models.FiltrationSearchResponse
import retrofit2.Call

class FiltrationSearchRepository(
    private val apiService: ApiService
) {
    fun filtrationSearch(
        token: String, page: Int,
        minPrice: String, maxPrice: String, rating: String,
        bedroom: String, bathroom: String, neighborhood: String
    ): Call<FiltrationSearchResponse> {
        return apiService.filtrationSearch (
            token, page,
            minPrice, maxPrice, rating,
            bedroom, bathroom, neighborhood
        )
    }
}
