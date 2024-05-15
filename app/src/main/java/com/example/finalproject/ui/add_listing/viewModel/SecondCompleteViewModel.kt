package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.SecondCompleteResponse
import com.example.finalproject.ui.add_listing.repository.SecondCompleteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecondCompleteViewModel(
    private val secondCompleteRepository: SecondCompleteRepository
): ViewModel() {
    val secondCompleteLiveData: MutableLiveData<SecondCompleteResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun secondComplete(
        token: String, residenceId: String, roofStyle: String, roofMatl: String,
        houseStyle: String, msSubClass: String, centralAir: String, street: String,
        alley: String, heating: String, heatingQc: String, masVnrType: String, masVnrArea: String,
        exterior1st: String, exterior2nd: String, exterCond: String, exterQual: String,
        condition1: String, condition2: String
    ){
        secondCompleteRepository.secondComplete(
            "Bearer $token", residenceId,
            roofStyle, roofMatl, houseStyle, msSubClass, centralAir, street, alley,
            heating, heatingQc, masVnrType, masVnrArea, exterior1st, exterior2nd,
            exterCond, exterQual, condition1, condition2
        )
            .enqueue(object : Callback<SecondCompleteResponse>{
                override fun onResponse(
                    call: Call<SecondCompleteResponse>,
                    response: Response<SecondCompleteResponse>
                ) {
                    if (response.isSuccessful){
                        secondCompleteLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<SecondCompleteResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}