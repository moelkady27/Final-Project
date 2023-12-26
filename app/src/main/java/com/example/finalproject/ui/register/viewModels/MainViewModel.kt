package com.example.finalproject.ui.register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.register.models.LogOutResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

//    val logOutResponseLiveData: MutableLiveData<LogOutResponse> = MutableLiveData()
//    val errorLiveData: MutableLiveData<String> = MutableLiveData()
//
//    fun logout(token: String){
//        RetrofitClient.instance.logout("Bearer $token")
//            .enqueue(object : Callback<LogOutResponse>{
//                override fun onResponse(
//                    call: Call<LogOutResponse>,
//                    response: Response<LogOutResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        logOutResponseLiveData.value = response.body()
//                    } else {
//                        errorLiveData.value = response.errorBody()?.string()
//                    }
//                }
//
//                override fun onFailure(call: Call<LogOutResponse>, t: Throwable) {
//                    errorLiveData.value = t.message
//                }
//            })
//    }
}