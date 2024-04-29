package com.example.finalproject.ui.complete_register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.complete_register.models.CompleteSignUpResponse
import com.example.finalproject.ui.complete_register.repository.CompleteSignUpRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompleteSignUpViewModel(
    private val completeSignUpRepository: CompleteSignUpRepository
) : ViewModel() {

    val completeSignUpResponseLiveData: MutableLiveData<CompleteSignUpResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun completeSignUp(token: String, firstName: String, lastName: String, gender: String, phoneNumber: String) {
        completeSignUpRepository.completeSignUp("Bearer $token", firstName, lastName, gender, phoneNumber)
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