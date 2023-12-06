package com.example.finalproject.ui.register.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.register.models.SignUpResponse
import com.example.finalproject.ui.register.request.SignUpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    val signUpResponseLiveData: MutableLiveData<SignUpResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun signUp(username: String, email: String, password: String, confirmPass: String) {
        val data = SignUpRequest(username, email, password, confirmPass)

        RetrofitClient.instance.signup(data)
            .enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                    if (response.isSuccessful) {
                        signUpResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}