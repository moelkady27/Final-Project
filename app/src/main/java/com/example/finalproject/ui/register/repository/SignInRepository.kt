package com.example.finalproject.ui.register.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.register.models.SignInResponse
import com.example.finalproject.ui.register.request.SignInRequest
import retrofit2.Call

class SignInRepository(
    private val apiService: ApiService
) {
    fun signIn(
        email: String,
        password: String
    ): Call<SignInResponse> {
        val data = SignInRequest(email, password)
        return apiService.login(data)
    }
}