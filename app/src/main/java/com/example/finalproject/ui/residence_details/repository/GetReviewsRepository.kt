package com.example.finalproject.ui.residence_details.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.residence_details.models.GetReviewsResponse
import retrofit2.Call

class GetReviewsRepository(
    private val apiService: ApiService
) {
    fun getReviews(
        token: String,
        residenceId: String
    ): Call<GetReviewsResponse> {
        return apiService.getReviews(token, residenceId)
    }
}
