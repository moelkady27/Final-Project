package com.example.finalproject.ui.password.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.password.repository.ResetPasswordRepository
import com.example.finalproject.ui.password.models.ResetPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordViewModel(
    private val resetPasswordRepository: ResetPasswordRepository
): ViewModel() {

    val resetPasswordResponseLiveData: MutableLiveData<ResetPasswordResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun resetPassword(email: String, password: String, confirmPass: String) {
        resetPasswordRepository.resetPassword(email, password, confirmPass)
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