package com.example.finalproject.ui.booking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.booking.models.PurchaseResidenceResponse
import com.example.finalproject.ui.booking.repository.PurchaseResidenceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchaseResidenceViewModel(
    private val purchaseResidenceRepository: PurchaseResidenceRepository
) : ViewModel() {

    val purchaseResidenceResponseLiveData: MutableLiveData<PurchaseResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var currentPage = 1
    private var isLastPage = false

    fun getPurchaseResidence(token: String) {
        if (loadingLiveData.value == true || isLastPage) {
            return
        }

        loadingLiveData.value = true
        purchaseResidenceRepository.getPurchaseResidence(token, currentPage)
            .enqueue(object : Callback<PurchaseResidenceResponse>{
                override fun onResponse(
                    call: Call<PurchaseResidenceResponse>,
                    response: Response<PurchaseResidenceResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        val data = response.body()
                        purchaseResidenceResponseLiveData.value = data!!
                        currentPage++
                        if (data?.residences?.isEmpty() == true) {
                            isLastPage = true
                        }
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<PurchaseResidenceResponse>, t: Throwable) {
                    loadingLiveData.value = false
                    errorLiveData.value = t.message
                }

            })
    }
}