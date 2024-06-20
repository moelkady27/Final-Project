package com.example.finalproject.ui.booking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.booking.models.MakeBookResponse
import com.example.finalproject.ui.booking.repository.MakeBookRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeBookViewModel(
    private val makeBookRepository: MakeBookRepository
): ViewModel() {

    val makeBookResponseLiveData: MutableLiveData<MakeBookResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun makeBook(token: String, residenceId: String){
        makeBookRepository.makeBook("Bearer $token", residenceId)
            .enqueue(object : Callback<MakeBookResponse>{
                override fun onResponse(
                    call: Call<MakeBookResponse>,
                    response: Response<MakeBookResponse>
                ) {
                    if (response.isSuccessful){
                        makeBookResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<MakeBookResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }

}