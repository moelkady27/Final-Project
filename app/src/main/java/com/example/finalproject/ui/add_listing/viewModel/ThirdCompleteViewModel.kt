package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.ThirdCompleteResponse
import com.example.finalproject.ui.add_listing.repository.ThirdCompleteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdCompleteViewModel(
    private val thirdCompleteRepository: ThirdCompleteRepository
): ViewModel() {
    val thirdCompleteLiveData: MutableLiveData<ThirdCompleteResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun thirdComplete(
        token: String, residenceId: String, hasGarage: String, garageType: String,
        garageQual: String, garageCars: String, garageFinish: String, hasBasement: String,
        bsmtUnfSF: String, bsmtExposure: String, bsmtFinType1: String, bsmtQual: String,
        bsmtCond: String, hasFireplace: String, fireplaces: String, fireplaceQu: String,
        bedroomAbvGr: String, totalbaths: String, KitchenAbvGr: String, kitchenQual: String,
        totRmsAbvGrd: String
    ){
        thirdCompleteRepository.thirdComplete("Bearer $token", residenceId,
            hasGarage, garageType, garageQual, garageCars,
            garageFinish, hasBasement, bsmtUnfSF, bsmtExposure, bsmtFinType1, bsmtQual,
            bsmtCond, hasFireplace, fireplaces, fireplaceQu, bedroomAbvGr, totalbaths,
            KitchenAbvGr, kitchenQual, totRmsAbvGrd
        )
            .enqueue(object : Callback<ThirdCompleteResponse> {
                override fun onResponse(
                    call: Call<ThirdCompleteResponse>,
                    response: Response<ThirdCompleteResponse>
                ) {
                    if (response.isSuccessful){
                        thirdCompleteLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ThirdCompleteResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}