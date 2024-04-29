package com.example.finalproject.ui.profile.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.profile.models.GetUserResponse
import retrofit2.Call

class GetUserInfoRepository(
    private val apiService: ApiService
) {
    fun getUserInfo(
        token: String
    ): Call<GetUserResponse>{
        return apiService.getProfile(token)
    }

}