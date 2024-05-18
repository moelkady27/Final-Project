package com.example.finalproject.ui.favourite.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.FirstCompleteResponse
import com.example.finalproject.ui.add_listing.repository.FirstCompleteRepository
import com.example.finalproject.ui.favourite.models.AddToFavouritesResponse
import com.example.finalproject.ui.favourite.repository.AddToFavouritesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToFavouritesViewModel(
    private val addToFavouritesRepository: AddToFavouritesRepository
): ViewModel() {
    val addToFavouritesLiveData: MutableLiveData<AddToFavouritesResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun addToFavourites(token: String, residenceId: String){
        addToFavouritesRepository.addToFavourites("Bearer $token", residenceId)
            .enqueue(object : Callback<AddToFavouritesResponse> {
                override fun onResponse(
                    call: Call<AddToFavouritesResponse>,
                    response: Response<AddToFavouritesResponse>
                ) {
                    if (response.isSuccessful){
                        addToFavouritesLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<AddToFavouritesResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}