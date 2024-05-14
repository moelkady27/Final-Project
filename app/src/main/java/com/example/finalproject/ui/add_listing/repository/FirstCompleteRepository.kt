package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.FirstCompleteResponse
import com.example.finalproject.ui.add_listing.request.FirstCompleteRequest
import retrofit2.Call

class FirstCompleteRepository(
    private val apiService: ApiService
) {
    fun firstComplete(
        token: String,
        residenceId: String,
        neighborhood: String,
        mszoning: String,
        saleCondition: String,
        moSold: String,
        salePrice: String,
        paymentPeriod: String,
        saleType: String,
        utilities: List<String>,
        lotShape: String,
        electrical: String,
        foundation: String,
        bldgType: String
    ): Call<FirstCompleteResponse> {
        val data = FirstCompleteRequest(
            neighborhood,
            mszoning,
            saleCondition,
            moSold,
            salePrice,
            paymentPeriod,
            saleType,
            utilities,
            lotShape,
            electrical,
            foundation,
            bldgType
        )
        return apiService.firstComplete(token, residenceId, data)
    }
}