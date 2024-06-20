package com.example.finalproject.ui.booking.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.booking.models.AcceptResponse
import retrofit2.Call

class AcceptRepository(
    private val apiService: ApiService
) {
    fun acceptBooking(
        token: String,
        residenceId: String,
        userId: String
    ): Call<AcceptResponse>{
        return apiService.acceptBook(token, residenceId, userId)
    }
}