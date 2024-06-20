package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.UpdatePriceResponse
import com.example.finalproject.ui.add_listing.request.UpdatePriceRequest
import retrofit2.Call

class UpdatePriceRepository(
    private val apiService: ApiService
) {
    fun updatePrice(
        token: String,
        residenceId: String,
        newPrice: Int
    ) : Call<UpdatePriceResponse> {
        val data = UpdatePriceRequest(newPrice)
        return apiService.updatePrice(token, residenceId, data)
    }
}