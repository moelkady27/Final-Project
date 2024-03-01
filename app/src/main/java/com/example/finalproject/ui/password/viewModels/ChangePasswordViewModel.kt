package com.example.finalproject.ui.password.viewModels

import android.media.session.MediaSession.Token
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.password.models.ChangePasswordResponse
import com.example.finalproject.ui.password.request.ChangePasswordRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel: ViewModel() {

    val changePasswordLiveData: MutableLiveData<ChangePasswordResponse> = MutableLiveData()
    val errorLiveDate: MutableLiveData<String> = MutableLiveData()

    fun changePassword(token: String , oldPassword: String , newPassword: String , confirmPass: String){
        val data = ChangePasswordRequest(oldPassword, newPassword, confirmPass)

        RetrofitClient.instance.changePass("Bearer $token" , data)
            .enqueue(object : Callback<ChangePasswordResponse>{
                override fun onResponse(
                    call: Call<ChangePasswordResponse>,
                    response: Response<ChangePasswordResponse>
                ) {
                    if (response.isSuccessful){
                        changePasswordLiveData.value = response.body()
                    }
                    else{
                        errorLiveDate.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    errorLiveDate.value = t.message
                }

            })
    }
}