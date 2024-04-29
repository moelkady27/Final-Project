package com.example.finalproject.ui.complete_register.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.complete_register.models.CompleteSignUpResponse
import com.example.finalproject.ui.complete_register.request.CompleteSignUpRequest
import retrofit2.Call

class CompleteSignUpRepository(
    private val apiService: ApiService
) {
    fun completeSignUp(
        token: String,
        firstName: String,
        lastName: String,
        gender: String,
        phoneNumber: String
    ): Call<CompleteSignUpResponse> {
        val data = CompleteSignUpRequest(firstName, lastName, gender, phoneNumber)
        return apiService.complete(token, data)
    }
}