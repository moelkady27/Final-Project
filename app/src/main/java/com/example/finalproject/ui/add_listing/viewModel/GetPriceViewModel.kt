package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.GetPriceResponse
import com.example.finalproject.ui.add_listing.repository.GetPriceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPriceViewModel(
    private val getPriceRepository: GetPriceRepository
): ViewModel() {

    val getPriceResponseLiveData: MutableLiveData<GetPriceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getPrice(token: String, residenceId: String) {
        getPriceRepository.getPrice("Bearer $token", residenceId)
            .enqueue(object : Callback<GetPriceResponse> {
                override fun onResponse(
                    call: Call<GetPriceResponse>,
                    response: Response<GetPriceResponse>
                ) {
                    if (response.isSuccessful){
                        getPriceResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetPriceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}