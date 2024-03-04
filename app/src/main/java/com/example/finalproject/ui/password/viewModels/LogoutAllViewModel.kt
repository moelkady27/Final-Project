package com.example.finalproject.ui.password.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.password.models.LogOutAllResponse
import com.example.finalproject.ui.setting.models.LogOutResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutAllViewModel: ViewModel() {

    val logOutAllResponseLiveData: MutableLiveData<LogOutAllResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun logoutAll(token: String){
        RetrofitClient.instance.logoutAll("Bearer $token")
            .enqueue(object : Callback<LogOutAllResponse> {
                override fun onResponse(
                    call: Call<LogOutAllResponse>,
                    response: Response<LogOutAllResponse>
                ) {
                    if (response.isSuccessful) {
                        logOutAllResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<LogOutAllResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}