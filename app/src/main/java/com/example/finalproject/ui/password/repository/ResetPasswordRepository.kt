package com.example.finalproject.ui.password.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.password.models.ResetPasswordResponse
import com.example.finalproject.ui.password.request.ResetPasswordRequest
import retrofit2.Call

class ResetPasswordRepository(
    private val apiService: ApiService
) {
    fun resetPassword(
        email: String,
        password: String,
        confirmPass: String
    ): Call<ResetPasswordResponse> {
        val data = ResetPasswordRequest(password, confirmPass)
        return apiService.resetPassword(email, data)
    }
}