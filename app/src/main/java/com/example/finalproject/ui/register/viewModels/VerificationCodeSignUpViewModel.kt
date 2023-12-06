package com.example.finalproject.ui.register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.register.models.ResendCodeResponse
import com.example.finalproject.ui.register.models.VerificationCodeSignUpResponse
import com.example.finalproject.ui.register.request.VerificationCodeSignUpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationCodeSignUpViewModel : ViewModel() {

    val verificationCodeSignUpResponseLiveData: MutableLiveData<VerificationCodeSignUpResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    val resendCodeResponseLiveData: MutableLiveData<ResendCodeResponse> = MutableLiveData()

    fun verifyAccount(token: String, userId: String, otp: Int) {
        val data = VerificationCodeSignUpRequest(otp)

        RetrofitClient.instance.verifyAccount("Bearer $token", userId, data)
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

    fun resendCode(token: String, userId: String) {
        RetrofitClient.instance.resendCode("Bearer $token", userId)
            .enqueue(object : Callback<ResendCodeResponse> {
                override fun onResponse(
                    call: Call<ResendCodeResponse>,
                    response: Response<ResendCodeResponse>
                ) {
                    if (response.isSuccessful) {
                        resendCodeResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResendCodeResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}