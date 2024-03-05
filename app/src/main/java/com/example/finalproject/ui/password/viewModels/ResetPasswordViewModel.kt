package com.example.finalproject.ui.password.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.password.models.ResetPasswordResponse
import com.example.finalproject.ui.password.request.ResetPasswordRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordViewModel: ViewModel() {

    val resetPasswordResponseLiveData: MutableLiveData<ResetPasswordResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun resetPassword(email: String, password: String, confirmPass: String) {
        val data = ResetPasswordRequest(password, confirmPass)

        RetrofitClient.instance.resetPassword(email, data)
            .enqueue(object : Callback<ResetPasswordResponse>{
                override fun onResponse(
                    call: Call<ResetPasswordResponse>,
                    response: Response<ResetPasswordResponse>
                ) {
                    if (response.isSuccessful){
                        resetPasswordResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }

}