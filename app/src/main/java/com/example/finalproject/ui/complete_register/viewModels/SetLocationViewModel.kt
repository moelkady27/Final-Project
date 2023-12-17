package com.example.finalproject.ui.complete_register.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.complete_register.models.LocationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetLocationViewModel : ViewModel() {

    val locationResponseLiveData: MutableLiveData<LocationResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun setLocation(token: String, longitude: Double, latitude: Double) {
        RetrofitClient.instance.location("Bearer $token", longitude, latitude)
            .enqueue(object : Callback<LocationResponse> {
                override fun onResponse(
                    call: Call<LocationResponse>,
                    response: Response<LocationResponse>
                ) {
                    if (response.isSuccessful) {
                        locationResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}