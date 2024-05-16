package com.example.finalproject.ui.add_listing.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.add_listing.models.ThirdCompleteResponse
import com.example.finalproject.ui.add_listing.request.ThirdCompleteRequest
import retrofit2.Call

class ThirdCompleteRepository(
    private val apiService: ApiService
) {
    fun thirdComplete(
        token: String,
        residenceId: String,
        hasGarage: String,
        garageType: String,
        garageQual: String,
        garageCars: String,
        garageFinish: String,
        hasBasement: String,
        bsmtUnfSF: String,
        bsmtExposure: String,
        bsmtFinType1: String,
        bsmtQual: String,
        bsmtCond: String,
        hasFireplace: String,
        fireplaces: String,
        fireplaceQu: String,
        bedroomAbvGr: String,
        totalbaths: String,
        KitchenAbvGr: String,
        kitchenQual: String,
        totRmsAbvGrd: String
    ): Call<ThirdCompleteResponse>{
        val data = ThirdCompleteRequest(
            hasGarage, garageType, garageQual, garageCars, garageFinish, hasBasement,
            bsmtUnfSF, bsmtExposure, bsmtFinType1, bsmtQual, bsmtCond, hasFireplace,
            fireplaces, fireplaceQu, bedroomAbvGr, totalbaths, KitchenAbvGr, kitchenQual,
            totRmsAbvGrd
        )
        return apiService.thirdComplete(token, residenceId, data)
    }
}