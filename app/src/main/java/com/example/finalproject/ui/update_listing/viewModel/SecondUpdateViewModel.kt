package com.example.finalproject.ui.update_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.repository.SecondUpdateRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecondUpdateViewModel(
    private val secondUpdateRepository: SecondUpdateRepository
): ViewModel() {
    val secondUpdateLiveData: MutableLiveData<UpdateResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun secondUpdate(
        token: String, residenceId: String,
        roofStyle: String, roofMatl: String, houseStyle: String, msSubClass: String,
        centralAir: String, street: String, alley: String, heating: String, heatingQc: String,
        masVnrType: String, masVnrArea: String, exterior1st: String, exterior2nd: String,
        exterCond: String, exterQual: String, condition1: String, condition2: String
    ) {
        secondUpdateRepository.secondUpdate(
            "Bearer $token", residenceId,
            roofStyle, roofMatl, houseStyle, msSubClass, centralAir, street, alley, heating, heatingQc,
            masVnrType, masVnrArea, exterior1st, exterior2nd, exterCond, exterQual, condition1, condition2
        )
            .enqueue(object : Callback<UpdateResidenceResponse> {
                override fun onResponse(
                    call: Call<UpdateResidenceResponse>,
                    response: Response<UpdateResidenceResponse>
                ) {
                    if (response.isSuccessful){
                        secondUpdateLiveData.value = response.body()
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