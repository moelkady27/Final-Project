package com.example.finalproject.ui.password.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.password.models.ForgotPasswordResponse
import com.example.finalproject.ui.password.request.ForgotPasswordRequest
import retrofit2.Call

class ForgotPasswordRepository(
    private val apiService: ApiService
) {
    fun forgotPassword(
        email: String
    ): Call<ForgotPasswordResponse> {
        val data = ForgotPasswordRequest(email)
        return apiService.forgotPassword(data)
    }
}