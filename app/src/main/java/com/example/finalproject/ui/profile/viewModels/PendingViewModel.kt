package com.example.finalproject.ui.profile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.profile.models.PendingResponse
import com.example.finalproject.ui.profile.repository.PendingRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingViewModel(
    private val pendingRepository: PendingRepository
): ViewModel() {
    val pendingResponseLiveData: MutableLiveData<PendingResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getPending(token: String) {
        pendingRepository.getPending(token)
            .enqueue(object : Callback<PendingResponse>{
                override fun onResponse(
                    call: Call<PendingResponse>,
                    response: Response<PendingResponse>
                ) {
                    if (response.isSuccessful) {
                        pendingResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody().toString()
                    }
                }

                override fun onFailure(call: Call<PendingResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}