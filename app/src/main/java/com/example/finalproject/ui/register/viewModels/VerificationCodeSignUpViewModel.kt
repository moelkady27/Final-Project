package com.example.finalproject.ui.register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.register.models.ResendCodeResponse
import com.example.finalproject.ui.register.models.VerificationCodeSignUpResponse
import com.example.finalproject.ui.register.repository.VerificationCodeSignUpRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationCodeSignUpViewModel(
    private val verificationCodeSignUpRepository: VerificationCodeSignUpRepository
) : ViewModel() {

    val verificationCodeSignUpResponseLiveData: MutableLiveData<VerificationCodeSignUpResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    val resendCodeResponseLiveData: MutableLiveData<ResendCodeResponse> = MutableLiveData()

    fun verifyAccount(token: String, otp: Int) {

        verificationCodeSignUpRepository.verifyAccount(token, otp)
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

    fun resendCode(token: String) {

        verificationCodeSignUpRepository.resendCode("Bearer $token")
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