package com.example.finalproject.ui.register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.register.models.SignInResponse
import com.example.finalproject.ui.register.repository.SignInRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(
    private val signInRepository: SignInRepository
) : ViewModel() {

    val signInResponseLiveData: MutableLiveData<SignInResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun signIn(email: String, password: String) {

        signInRepository.signIn(email, password)
            .enqueue(object : Callback<SignInResponse>{
                override fun onResponse(
                    call: Call<SignInResponse>,
                    response: Response<SignInResponse>
                ) {
                    if (response.isSuccessful) {
                        signInResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}