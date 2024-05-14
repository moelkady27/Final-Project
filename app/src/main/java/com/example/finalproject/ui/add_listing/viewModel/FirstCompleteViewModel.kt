package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.FirstCompleteResponse
import com.example.finalproject.ui.add_listing.repository.FirstCompleteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstCompleteViewModel(
    private val firstCompleteRepository: FirstCompleteRepository
): ViewModel() {
    val firstCompleteLiveData: MutableLiveData<FirstCompleteResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun firstComplete(token: String, residenceId: String, neighborhood: String, mszoning: String,
                      saleCondition: String, moSold: String, salePrice: String, paymentPeriod: String,
                      saleType: String, utilities: List<String>, lotShape: String, electrical: String,
                      foundation: String, bldgType: String){
        firstCompleteRepository.firstComplete("Bearer $token", residenceId,
                        neighborhood, mszoning, saleCondition, moSold, salePrice,
                        paymentPeriod, saleType, utilities, lotShape, electrical,
                        foundation, bldgType)
            .enqueue(object : Callback<FirstCompleteResponse>{
                override fun onResponse(
                    call: Call<FirstCompleteResponse>,
                    response: Response<FirstCompleteResponse>
                ) {
                    if (response.isSuccessful){
                        firstCompleteLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<FirstCompleteResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}