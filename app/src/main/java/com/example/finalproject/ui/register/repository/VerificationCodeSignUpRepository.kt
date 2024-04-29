package com.example.finalproject.ui.register.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.register.models.ResendCodeResponse
import com.example.finalproject.ui.register.models.VerificationCodeSignUpResponse
import com.example.finalproject.ui.register.request.VerificationCodeSignUpRequest
import retrofit2.Call

class VerificationCodeSignUpRepository(
    private val apiService: ApiService
) {
    fun verifyAccount(
        token: String,
        otp: Int
    ): Call<VerificationCodeSignUpResponse> {
        val data = VerificationCodeSignUpRequest(otp)
        return apiService.verifyAccount("Bearer $token", data)
    }

    fun resendCode(
        token: String
    ): Call<ResendCodeResponse> {
        return apiService.resendCode("Bearer $token")
    }
}