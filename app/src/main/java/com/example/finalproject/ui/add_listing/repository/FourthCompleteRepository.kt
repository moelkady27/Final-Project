package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.FourthCompleteResponse
import com.example.finalproject.ui.add_listing.request.FourthCompleteRequest
import retrofit2.Call

class FourthCompleteRepository(
    private val apiService: ApiService
) {
    fun fourthComplete(
        token: String, residenceId: String,
        lotConfig: String, landContour: String, landSlope: String, pavedDrive: String,
        poolArea: String, overallQual: String, overallCond: String, totalarea: String,
        totalporchsf: String, lotArea: String, lotFrontage: String, totalsf: String,
        lowQualFinSF: String, miscVal: String, houseage: String, houseremodelage: String
    ): Call<FourthCompleteResponse> {
        val data = FourthCompleteRequest(
            lotConfig, landContour,landSlope, pavedDrive,
            poolArea, overallQual, overallCond, totalarea,
            totalporchsf, lotArea, lotFrontage, totalsf,
            lowQualFinSF, miscVal, houseage, houseremodelage
        )
        return apiService.fourthComplete(token, residenceId, data)
    }
}