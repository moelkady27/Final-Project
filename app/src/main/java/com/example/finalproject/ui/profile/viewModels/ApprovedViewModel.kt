package com.example.finalproject.ui.profile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.profile.models.ResidenceResponse
import com.example.finalproject.ui.profile.repository.ApprovedRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApprovedViewModel(
    private val approvedRepository: ApprovedRepository
): ViewModel() {
    val approvedResponseLiveData: MutableLiveData<ResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var currentPage = 1
    private var isLastPage = false

    fun getApproved(token: String) {
        if (loadingLiveData.value == true || isLastPage) {
            return
        }

        loadingLiveData.value = true
        approvedRepository.getApproved(token, currentPage)
            .enqueue(object : Callback<ResidenceResponse>{
                override fun onResponse(
                    call: Call<ResidenceResponse>,
                    response: Response<ResidenceResponse>)
                {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        val data = response.body()
                        approvedResponseLiveData.value = data!!
                        currentPage++
                        if (data?.residences?.isEmpty() == true) {
                            isLastPage = true
                        }
                    } else {
                        errorLiveData.value = response.errorBody().toString()
                    }
                }

                override fun onFailure(call: Call<ResidenceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}