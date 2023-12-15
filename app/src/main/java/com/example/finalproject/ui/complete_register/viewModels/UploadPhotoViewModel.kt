package com.example.finalproject.ui.complete_register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.complete_register.models.UploadPhotoResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadPhotoViewModel : ViewModel() {

    val uploadPhotoResponseLiveData: MutableLiveData<UploadPhotoResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun uploadPhoto(token: String, image: MultipartBody.Part) {
        RetrofitClient.instance.uploadImage("Bearer $token", image)
            .enqueue(object : Callback<UploadPhotoResponse> {
                override fun onResponse(
                    call: Call<UploadPhotoResponse>,
                    response: Response<UploadPhotoResponse>
                ) {
                    if (response.isSuccessful) {
                        uploadPhotoResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<UploadPhotoResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}