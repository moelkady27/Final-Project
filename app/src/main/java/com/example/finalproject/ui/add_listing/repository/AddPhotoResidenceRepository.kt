package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.UploadPhotoResidenceResponse
import okhttp3.MultipartBody
import retrofit2.Call

class AddPhotoResidenceRepository(
    private val apiService: ApiService
) {
    fun uploadResidencePhoto(
        token: String,
        residenceId: String,
        images: MultipartBody.Part
    ): Call<UploadPhotoResidenceResponse> {
        return apiService.uploadResidenceImage(token, residenceId, images)
    }
}