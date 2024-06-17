package com.example.finalproject.ui.residence_details.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.residence_details.models.LikeReviewResponse
import retrofit2.Call

class RemoveLikeRepository(
    private val apiService: ApiService
) {
    fun removeLike(
        token: String, reviewId: String
    ): Call<LikeReviewResponse> {
        return apiService.removeLike(token, reviewId)
    }
}