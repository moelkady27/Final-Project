package com.example.finalproject.ui.booking.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.booking.models.PurchaseResidenceResponse
import retrofit2.Call

class PurchaseResidenceRepository(
    private val apiService: ApiService
) {
    fun getPurchaseResidence(
        token: String,
        page: Int
    ): Call<PurchaseResidenceResponse> {
        return apiService.getPurchaseResidence(token, page)
    }
}