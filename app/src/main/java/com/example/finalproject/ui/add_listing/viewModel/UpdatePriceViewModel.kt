package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.UpdatePriceResponse
import com.example.finalproject.ui.add_listing.repository.UpdatePriceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePriceViewModel(
    private val updatePriceRepository: UpdatePriceRepository
): ViewModel(){

    val updatePriceResponseLiveData: MutableLiveData<UpdatePriceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun updatePrice(token: String, residenceId: String, newPrice: Int){
        updatePriceRepository.updatePrice("Bearer $token", residenceId, newPrice)
            .enqueue(object : Callback<UpdatePriceResponse>{
                override fun onResponse(
                    call: Call<UpdatePriceResponse>,
                    response: Response<UpdatePriceResponse>
                ) {
                    if (response.isSuccessful){
                        updatePriceResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<UpdatePriceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}