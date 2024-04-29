package com.example.finalproject.ui.password.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.password.models.ResendCodeForgetResponse
import com.example.finalproject.ui.password.models.VerificationCodeForgetPasswordResponse
import com.example.finalproject.ui.password.request.VerificationCodeForgetPasswordRequest
import retrofit2.Call

class VerificationCodeForgetRepository(
    private val apiService: ApiService
) {
    fun verifyForget(
        otp :Int,
        email: String
    ): Call<VerificationCodeForgetPasswordResponse> {
        val data = VerificationCodeForgetPasswordRequest(otp)
        return apiService.verificationCodeForgetPass(data, email)
    }

    fun resendCodeForget(
        email: String
    ): Call<ResendCodeForgetResponse> {
        return apiService.resendCodeForget(email)
    }

}