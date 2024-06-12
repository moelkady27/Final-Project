package com.example.finalproject.ui.update_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.repository.FourthUpdateRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FourthUpdateViewModel(
    private val fourthUpdateRepository: FourthUpdateRepository
): ViewModel() {
    val fourthUpdateLiveData: MutableLiveData<UpdateResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun fourthUpdate(
        token: String, residenceId: String,
        lotConfig: String, landContour: String, landSlope: String, pavedDrive: String,
        poolArea: String, overallQual: String, overallCond: String, totalarea: String,
        totalporchsf: String, lotArea: String, lotFrontage: String, totalsf: String,
        lowQualFinSF: String, miscVal: String, houseage: String, houseremodelage: String
    ) {
        fourthUpdateRepository.fourthUpdate(
            "Bearer $token", residenceId,
            lotConfig, landContour,landSlope, pavedDrive,
            poolArea, overallQual, overallCond, totalarea,
            totalporchsf, lotArea, lotFrontage, totalsf,
            lowQualFinSF, miscVal, houseage, houseremodelage
        )
            .enqueue(object : Callback<UpdateResidenceResponse> {
                override fun onResponse(
                    call: Call<UpdateResidenceResponse>,
                    response: Response<UpdateResidenceResponse>
                ) {
                    if (response.isSuccessful){
                        fourthUpdateLiveData.value = response.body()
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