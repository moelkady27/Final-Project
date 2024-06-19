package com.example.finalproject.ui.residence_details.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.residence_details.models.LikeReviewResponse
import retrofit2.Call

class RemoveUnLikeRepository(
    private val apiService: ApiService
) {
    fun removeUnLike(
        token: String, reviewId: String
    ): Call<LikeReviewResponse> {
        return apiService.removeUnlike(token, reviewId)
    }
}