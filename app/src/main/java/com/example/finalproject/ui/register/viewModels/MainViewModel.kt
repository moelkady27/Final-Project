package com.example.finalproject.ui.register.viewModels

import androidx.lifecycle.ViewModel

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