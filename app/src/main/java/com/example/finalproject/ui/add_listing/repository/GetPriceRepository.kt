package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.GetPriceResponse
import retrofit2.Call

class GetPriceRepository (
    private val apiService: ApiService
) {
    fun getPrice(
        token: String,
        residenceId: String
    ): Call<GetPriceResponse>{
        return apiService.getPrice(token, residenceId)
    }

}