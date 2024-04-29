package com.example.finalproject.ui.setting.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.setting.models.LogOutResponse
import com.example.finalproject.ui.setting.repository.LogOutRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogOutViewModel(
    private val logOutRepository: LogOutRepository
): ViewModel() {

    val logOutResponseLiveData: MutableLiveData<LogOutResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun logout(token: String){
        logOutRepository.logout("Bearer $token")
            .enqueue(object : Callback<LogOutResponse> {
                override fun onResponse(
                    call: Call<LogOutResponse>,
                    response: Response<LogOutResponse>
                ) {
                    if (response.isSuccessful) {
                        logOutResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<LogOutResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}