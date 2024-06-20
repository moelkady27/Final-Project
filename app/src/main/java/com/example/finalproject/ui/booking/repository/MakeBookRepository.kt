package com.example.finalproject.ui.booking.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.booking.models.MakeBookResponse
import retrofit2.Call

class MakeBookRepository(
    private val apiService: ApiService
) {
    fun makeBook(
        token: String,
        residenceId: String,
    ) : Call<MakeBookResponse>{
        return apiService.makeBook(token, residenceId)
    }
}