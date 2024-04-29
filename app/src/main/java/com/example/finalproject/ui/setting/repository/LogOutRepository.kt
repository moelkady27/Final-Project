package com.example.finalproject.ui.setting.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.setting.models.LogOutResponse
import retrofit2.Call

class LogOutRepository(
    private val apiService: ApiService
) {
    fun logout(
        token: String
    ): Call<LogOutResponse>{
        return apiService.logout(token)
    }
}