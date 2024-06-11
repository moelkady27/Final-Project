package com.example.finalproject.ui.update_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.request.FirstUpdateRequest
import retrofit2.Call

class FirstUpdateRepository(
    private val apiService: ApiService
) {
    fun firstUpdate(
        token: String, residenceId: String,
        neighborhood: String, mszoning: String, saleCondition: String,
        moSold: String, salePrice: String, paymentPeriod: String,
        saleType: String, utilities: List<String>, lotShape: String,
        electrical: String, foundation: String, bldgType: String
    ): Call<UpdateResidenceResponse> {
        val data = FirstUpdateRequest(
            neighborhood, mszoning, saleCondition, moSold,
            salePrice, paymentPeriod, saleType, utilities,
            lotShape, electrical, foundation, bldgType
        )
        return apiService.firstUpdate(data, token, residenceId)
    }
}