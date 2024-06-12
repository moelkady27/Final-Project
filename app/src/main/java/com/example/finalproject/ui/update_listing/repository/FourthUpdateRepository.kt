package com.example.finalproject.ui.update_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.request.FourthUpdateRequest
import retrofit2.Call

class FourthUpdateRepository(
    private val apiService: ApiService
) {
    fun fourthUpdate(
        token: String, residenceId: String,
        lotConfig: String, landContour: String, landSlope: String, pavedDrive: String,
        poolArea: String, overallQual: String, overallCond: String, totalarea: String,
        totalporchsf: String, lotArea: String, lotFrontage: String, totalsf: String,
        lowQualFinSF: String, miscVal: String, houseage: String, houseremodelage: String
    ): Call<UpdateResidenceResponse> {
        val data = FourthUpdateRequest(
            lotConfig, landContour,landSlope, pavedDrive,
            poolArea, overallQual, overallCond, totalarea,
            totalporchsf, lotArea, lotFrontage, totalsf,
            lowQualFinSF, miscVal, houseage, houseremodelage
        )
        return apiService.fourthUpdate(data, token, residenceId)
    }
}