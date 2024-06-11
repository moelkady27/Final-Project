package com.example.finalproject.ui.update_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.repository.FirstUpdateRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstUpdateViewModel(
    private val firstUpdateRepository: FirstUpdateRepository
): ViewModel() {
    val firstUpdateLiveData: MutableLiveData<UpdateResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun firstUpdate(
        token: String, residenceId: String, neighborhood: String, mszoning: String,
        saleCondition: String, moSold: String, salePrice: String, paymentPeriod: String,
        saleType: String, utilities: List<String>, lotShape: String, electrical: String,
        foundation: String, bldgType: String
    ) {
        firstUpdateRepository.firstUpdate(
            "Bearer $token", residenceId,
            neighborhood, mszoning, saleCondition, moSold, salePrice,
            paymentPeriod, saleType, utilities, lotShape, electrical,
            foundation, bldgType
        )
            .enqueue(object : Callback<UpdateResidenceResponse> {
                override fun onResponse(
                    call: Call<UpdateResidenceResponse>,
                    response: Response<UpdateResidenceResponse>
                ) {
                    if (response.isSuccessful){
                        firstUpdateLiveData.value = response.body()
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