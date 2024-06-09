package com.example.finalproject.ui.update_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.update_listing.models.DeleteResidenceImageResponse
import retrofit2.Call

class DeleteResidenceImageRepository(
    private val apiService: ApiService
) {
    fun deleteImage(
        token: String,
        imageId: String
    ): Call<DeleteResidenceImageResponse>{
        return apiService.deleteResidenceImage(token, imageId)
    }
}