package com.example.finalproject.ui.booking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.booking.models.AcceptResponse
import com.example.finalproject.ui.booking.repository.AcceptRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcceptViewModel(
    private val acceptRepository: AcceptRepository
): ViewModel() {

    val acceptResponseLiveData: MutableLiveData<AcceptResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun acceptBooking(token: String, residenceId: String, userId: String) {
        acceptRepository.acceptBooking("Bearer $token", residenceId, userId)
            .enqueue(object : Callback<AcceptResponse>{
                override fun onResponse(
                    call: Call<AcceptResponse>,
                    response: Response<AcceptResponse>
                ) {
                    if (response.isSuccessful){
                        acceptResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<AcceptResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}