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

    fun getSold(token: String) {
        soldRepository.getSold(token)
            .enqueue(object : Callback<ResidenceResponse>{
                override fun onResponse(
                    call: Call<ResidenceResponse>,
                    response: Response<ResidenceResponse>
                ) {
                    if (response.isSuccessful) {
                        soldResponseLiveData.value = response.body()
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