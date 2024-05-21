package com.example.finalproject.ui.profile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.profile.models.ResidenceResponse
import com.example.finalproject.ui.profile.repository.PendingRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingViewModel(
    private val pendingRepository: PendingRepository
): ViewModel() {
    val pendingResponseLiveData: MutableLiveData<ResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var currentPage = 1
    private var isLastPage = false

    fun getPending(token: String) {
        if (loadingLiveData.value == true || isLastPage) {
            return
        }

        loadingLiveData.value = true
        pendingRepository.getPending("Bearer $token", currentPage)
            .enqueue(object : Callback<ResidenceResponse>{
                override fun onResponse(
                    call: Call<ResidenceResponse>,
                    response: Response<ResidenceResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        val data = response.body()
                        pendingResponseLiveData.value = data!!
                        currentPage++
                        if (data?.residences?.isEmpty() == true) {
                            isLastPage = true
                        }
                    } else {
                        errorLiveData.value = response.errorBody().toString()
                    }
                }

                override fun onFailure(call: Call<ResidenceResponse>, t: Throwable) {
                    loadingLiveData.value = false
                    errorLiveData.value = t.message
                }

            })
    }

    fun resetPagination() {
        currentPage = 1
        isLastPage = false
    }
}