package com.example.finalproject.ui.update_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.repository.ThirdUpdateRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdUpdateViewModel(
    private val thirdUpdateRepository: ThirdUpdateRepository
): ViewModel() {
    val thirdUpdateLiveData: MutableLiveData<UpdateResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun thirdUpdate(
        token: String, residenceId: String,
        hasGarage: String, garageType: String, garageQual: String, garageCars: String,
        garageFinish: String, hasBasement: String, bsmtUnfSF: String, bsmtExposure: String,
        bsmtFinType1: String, bsmtQual: String, bsmtCond: String, hasFireplace: String,
        fireplaces: String, fireplaceQu: String, bedroomAbvGr: String, totalbaths: String,
        KitchenAbvGr: String, kitchenQual: String, totRmsAbvGrd: String
    ) {
        thirdUpdateRepository.thirdUpdate(
            "Bearer $token", residenceId,
            hasGarage, garageType, garageQual, garageCars, garageFinish, hasBasement,
            bsmtUnfSF, bsmtExposure, bsmtFinType1, bsmtQual, bsmtCond, hasFireplace,
            fireplaces, fireplaceQu, bedroomAbvGr, totalbaths, KitchenAbvGr, kitchenQual,
            totRmsAbvGrd
        )
            .enqueue(object : Callback<UpdateResidenceResponse> {
                override fun onResponse(
                    call: Call<UpdateResidenceResponse>,
                    response: Response<UpdateResidenceResponse>
                ) {
                    if (response.isSuccessful){
                        thirdUpdateLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<UpdateResidenceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}