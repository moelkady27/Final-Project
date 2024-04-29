package com.example.finalproject.ui.register.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.register.models.SignUpResponse
import com.example.finalproject.ui.register.request.SignUpRequest
import retrofit2.Call

class SignUpRepository(
    private val apiService: ApiService
) {
    fun signUp(
        username: String,
        email: String,
        password: String,
        confirmPass: String
    ): Call<SignUpResponse> {
        val data = SignUpRequest(username, email, password, confirmPass)
        return apiService.signup(data)
    }
}