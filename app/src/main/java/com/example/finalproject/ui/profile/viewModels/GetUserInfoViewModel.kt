package com.example.finalproject.ui.profile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.profile.models.GetUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetUserInfoViewModel: ViewModel() {

    val getUserInfoResponseLiveData: MutableLiveData<GetUserResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getUserInfo(token: String){

        RetrofitClient.instance.getProfile("Bearer $token")
            .enqueue(object : Callback<GetUserResponse>{
                override fun onResponse(
                    call: Call<GetUserResponse>,
                    response: Response<GetUserResponse>
                ) {
                    if (response.isSuccessful){
                        getUserInfoResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}