package com.example.finalproject.ui.password.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.password.models.VerificationCodeForgetPasswordResponse
import com.example.finalproject.ui.password.request.VerificationCodeForgetPasswordRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationCodeForgetPasswordViewModel: ViewModel() {

    val verificationCodeForgetResponseLiveData:
            MutableLiveData<VerificationCodeForgetPasswordResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun verifyForget(otp :Int , email: String){

        val data = VerificationCodeForgetPasswordRequest(otp)

        RetrofitClient.instance.verificationCodeForgetPass(data , email)
            .enqueue(object : Callback<VerificationCodeForgetPasswordResponse>{
                override fun onResponse(
                    call: Call<VerificationCodeForgetPasswordResponse>,
                    response: Response<VerificationCodeForgetPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        verificationCodeForgetResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<VerificationCodeForgetPasswordResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}