package com.example.finalproject.ui.setting.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.setting.models.DeleteAccountResponse
import com.example.finalproject.ui.setting.request.DeleteAccountRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountViewModel: ViewModel() {

    val deleteAccountResponseLiveData: MutableLiveData<DeleteAccountResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun deleteAccount(token: String, password: String){
        val data = DeleteAccountRequest(password)

        RetrofitClient.instance.deleteAccount("Bearer $token",data)
            .enqueue(object: Callback<DeleteAccountResponse>{
                override fun onResponse(
                    call: Call<DeleteAccountResponse>,
                    response: Response<DeleteAccountResponse>
                ) {
                    if (response.isSuccessful){
                        deleteAccountResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<DeleteAccountResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }

}