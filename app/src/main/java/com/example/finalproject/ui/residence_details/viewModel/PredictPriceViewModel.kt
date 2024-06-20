package com.example.finalproject.ui.residence_details.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.residence_details.models.PredictPriceResponse
import com.example.finalproject.ui.residence_details.repository.PredictPriceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictPriceViewModel(
    private val predictPriceRepository: PredictPriceRepository
): ViewModel() {

    val predictPriceResponseLiveData: MutableLiveData<PredictPriceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun predictPrice(token: String, residenceId: String) {
        predictPriceRepository.predictPrice("Bearer $token", residenceId)
            .enqueue(object : Callback<PredictPriceResponse>{
                override fun onResponse(
                    call: Call<PredictPriceResponse>,
                    response: Response<PredictPriceResponse>
                ) {
                    if (response.isSuccessful) {
                        predictPriceResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<PredictPriceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}