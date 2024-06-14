package com.example.finalproject.ui.residence_details.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.residence_details.models.AddReviewResponse
import com.example.finalproject.ui.residence_details.request.AddReviewRequest
import retrofit2.Call

class AddReviewRepository(
    private val apiService: ApiService
) {
    fun addReview(
        token: String, residenceId: String, rating: String, comment: String
    ): Call<AddReviewResponse> {
        val data = AddReviewRequest(
            rating, comment
        )
        return apiService.addReview(data, token, residenceId)
    }
}