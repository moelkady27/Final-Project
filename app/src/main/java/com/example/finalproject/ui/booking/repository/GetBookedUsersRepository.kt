package com.example.finalproject.ui.booking.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.booking.models.GetBookedUsersResponse
import retrofit2.Call

class GetBookedUsersRepository(
    private val apiService: ApiService
) {
    fun getBookedUsers(
        token: String,
        residenceId: String,
    ) : Call<GetBookedUsersResponse> {
        return apiService.getBookedUsers(token, residenceId)
    }
}