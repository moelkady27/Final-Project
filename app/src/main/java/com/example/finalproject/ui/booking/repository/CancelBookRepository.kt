package com.example.finalproject.ui.booking.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.booking.models.CancelBookResponse
import retrofit2.Call

class CancelBookRepository(
    private val apiService: ApiService
) {

    fun cancelBook(
        token: String,
        residenceId: String,
        userId: String
    ): Call<CancelBookResponse>{
        return apiService.cancelBook(token, residenceId, userId)
    }
}