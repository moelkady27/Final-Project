package com.example.finalproject.ui.password.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.password.models.ForgotPasswordResponse
import com.example.finalproject.ui.password.request.ForgotPasswordRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel: ViewModel() {

    val forgotPasswordResponseLiveData: MutableLiveData<ForgotPasswordResponse> = MutableLiveData()
    val errorLiveDate: MutableLiveData<String> = MutableLiveData()

    fun forgotPass(email: String){
        val data = ForgotPasswordRequest(email)
        RetrofitClient.instance.forgotPassword( data)
            .enqueue(object : Callback<ForgotPasswordResponse>{
                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    if (response.isSuccessful){
                        forgotPasswordResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveDate.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    errorLiveDate.value = t.message
                }

            })
    }
}