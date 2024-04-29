package com.example.finalproject.ui.complete_register.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.complete_register.models.UploadPhotoResponse
import okhttp3.MultipartBody
import retrofit2.Call

class UploadPhotoRepository(
    private val apiService: ApiService
) {
    fun uploadPhoto(
        token: String,
        image: MultipartBody.Part
    ): Call<UploadPhotoResponse> {
        return apiService.uploadImage(token, image)
    }
}