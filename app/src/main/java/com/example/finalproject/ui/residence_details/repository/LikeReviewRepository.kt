package com.example.finalproject.ui.residence_details.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.residence_details.models.LikeReviewResponse
import retrofit2.Call

class LikeReviewRepository(
    private val apiService: ApiService
) {
    fun likeReview(
        token: String, reviewId: String
    ): Call<LikeReviewResponse> {
        return apiService.likeReview(token, reviewId)
    }
}