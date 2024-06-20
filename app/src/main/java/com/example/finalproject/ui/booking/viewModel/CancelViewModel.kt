package com.example.finalproject.ui.booking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.booking.models.CancelBookResponse
import com.example.finalproject.ui.booking.repository.CancelBookRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CancelViewModel(
    private val cancelBookRepository: CancelBookRepository
): ViewModel() {

    val cancelResponseLiveData: MutableLiveData<CancelBookResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun cancelBooking(token: String, residenceId: String, userId: String) {
        cancelBookRepository.cancelBook("Bearer $token", residenceId, userId)
            .enqueue(object : Callback<CancelBookResponse>{
                override fun onResponse(
                    call: Call<CancelBookResponse>,
                    response: Response<CancelBookResponse>
                ) {
                    if (response.isSuccessful){
                        cancelResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<CancelBookResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}