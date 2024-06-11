package com.example.finalproject.ui.update_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.request.ThirdUpdateRequest
import retrofit2.Call

class ThirdUpdateRepository(
    private val apiService: ApiService
) {
    fun thirdUpdate(
        token: String, residenceId: String,
        hasGarage: String, garageType: String, garageQual: String, garageCars: String,
        garageFinish: String, hasBasement: String, bsmtUnfSF: String, bsmtExposure: String,
        bsmtFinType1: String, bsmtQual: String, bsmtCond: String, hasFireplace: String,
        fireplaces: String, fireplaceQu: String, bedroomAbvGr: String, totalbaths: String,
        KitchenAbvGr: String, kitchenQual: String, totRmsAbvGrd: String
    ): Call<UpdateResidenceResponse> {
        val data = ThirdUpdateRequest(
            hasGarage, garageType, garageQual, garageCars, garageFinish, hasBasement,
            bsmtUnfSF, bsmtExposure, bsmtFinType1, bsmtQual, bsmtCond, hasFireplace,
            fireplaces, fireplaceQu, bedroomAbvGr, totalbaths, KitchenAbvGr, kitchenQual,
            totRmsAbvGrd
        )
        return apiService.thirdUpdate(data, token, residenceId)
    }
}