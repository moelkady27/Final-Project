package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.FourthCompleteResponse
import com.example.finalproject.ui.add_listing.repository.FourthCompleteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FourthCompleteViewModel(
    private val fourthCompleteRepository: FourthCompleteRepository
): ViewModel() {
    val fourthCompleteLiveData: MutableLiveData<FourthCompleteResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun fourthComplete(
        token: String, residenceId: String,
        lotConfig: String, landContour: String, landSlope: String, pavedDrive: String,
        poolArea: String, overallQual: String, overallCond: String, totalarea: String,
        totalporchsf: String, lotArea: String, lotFrontage: String, totalsf: String,
        lowQualFinSF: String, miscVal: String, houseage: String, houseremodelage: String
    ){
        fourthCompleteRepository.fourthComplete(
                    "Bearer $token", residenceId,
                    lotConfig, landContour, landSlope, pavedDrive,
                    poolArea, overallQual, overallCond, totalarea,
                    totalporchsf, lotArea, lotFrontage, totalsf,
                    lowQualFinSF, miscVal, houseage, houseremodelage)
            .enqueue(object : Callback<FourthCompleteResponse>{
                override fun onResponse(
                    call: Call<FourthCompleteResponse>,
                    response: Response<FourthCompleteResponse>
                ) {
                    if (response.isSuccessful){
                        fourthCompleteLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<FourthCompleteResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}