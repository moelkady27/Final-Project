package com.example.finalproject.ui.register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.register.models.CompleteSignUpResponse
import com.example.finalproject.ui.register.request.CompleteSignUpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompleteSignUpViewModel : ViewModel() {

    val completeSignUpResponseLiveData: MutableLiveData<CompleteSignUpResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun completeSignUp(token: String, firstName: String, lastName: String, gender: String, phoneNumber: String) {
        val data = CompleteSignUpRequest(firstName, lastName, gender, phoneNumber)

        RetrofitClient.instance.complete("Bearer $token", data)
            .enqueue(object : Callback<CompleteSignUpResponse> {
                override fun onResponse(
                    call: Call<CompleteSignUpResponse>,
                    response: Response<CompleteSignUpResponse>
                ) {
                    if (response.isSuccessful) {
                        completeSignUpResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<CompleteSignUpResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}