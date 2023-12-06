package com.example.finalproject.ui.register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.register.models.VerificationCodeSignUpResponse
import com.example.finalproject.ui.register.request.VerificationCodeSignUpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationCodeSignUpViewModel : ViewModel(){

    val verificationCodeSignUpResponseLiveData: MutableLiveData<VerificationCodeSignUpResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun verifyAccount(userId: String, otp: Int) {
        val data = VerificationCodeSignUpRequest(otp)

        RetrofitClient.instance.verifyAccount(userId, data)
            .enqueue(object : Callback<VerificationCodeSignUpResponse> {
                override fun onResponse(
                    call: Call<VerificationCodeSignUpResponse>,
                    response: Response<VerificationCodeSignUpResponse>
                ) {
                    if (response.isSuccessful) {
                        verificationCodeSignUpResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<VerificationCodeSignUpResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}