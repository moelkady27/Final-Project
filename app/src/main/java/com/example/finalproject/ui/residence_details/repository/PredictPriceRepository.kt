package com.example.finalproject.ui.residence_details.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.residence_details.models.PredictPriceResponse
import retrofit2.Call

class PredictPriceRepository(
    private val apiService: ApiService
) {
    fun predictPrice(
        token: String,
        residenceId: String,
    ): Call<PredictPriceResponse>{
        return apiService.predictPrice(token, residenceId)
    }
}