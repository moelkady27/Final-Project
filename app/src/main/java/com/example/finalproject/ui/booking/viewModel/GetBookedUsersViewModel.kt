package com.example.finalproject.ui.booking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.booking.models.GetBookedUsersResponse
import com.example.finalproject.ui.booking.models.MakeBookResponse
import com.example.finalproject.ui.booking.repository.GetBookedUsersRepository
import com.example.finalproject.ui.booking.repository.MakeBookRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetBookedUsersViewModel(
    private val getBookedUsersRepository: GetBookedUsersRepository
): ViewModel() {

    val getBookedUsersResponseLiveData: MutableLiveData<GetBookedUsersResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getBookedUsers(token: String, residenceId: String){
        getBookedUsersRepository.getBookedUsers("Bearer $token", residenceId)
            .enqueue(object : Callback<GetBookedUsersResponse> {
                override fun onResponse(
                    call: Call<GetBookedUsersResponse>,
                    response: Response<GetBookedUsersResponse>
                ) {
                    if (response.isSuccessful){
                        getBookedUsersResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetBookedUsersResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}