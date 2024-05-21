package com.example.finalproject.ui.profile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.profile.models.ResidenceResponse
import com.example.finalproject.ui.profile.repository.SoldRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoldViewModel(
    private val soldRepository: SoldRepository
): ViewModel() {
    val soldResponseLiveData: MutableLiveData<ResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var currentPage = 1
    private var isLastPage = false

    fun getSold(token: String) {
        if (loadingLiveData.value == true || isLastPage) {
            return
        }

        loadingLiveData.value = true
        soldRepository.getSold("Bearer $token", currentPage)
            .enqueue(object : Callback<ResidenceResponse>{
                override fun onResponse(
                    call: Call<ResidenceResponse>,
                    response: Response<ResidenceResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        val data = response.body()
                        soldResponseLiveData.value = data!!
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