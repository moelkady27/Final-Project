package com.example.finalproject.ui.favourite.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.favourite.models.GetAllFavouritesResponse
import com.example.finalproject.ui.favourite.repository.AllFavouritesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllFavouritesViewModel(
    private val allFavouritesRepository: AllFavouritesRepository
): ViewModel() {
    val getFavouritesLiveData: MutableLiveData<GetAllFavouritesResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getFavourites(token: String){
        allFavouritesRepository.getFavourites("Bearer $token")
            .enqueue(object : Callback<GetAllFavouritesResponse> {
                override fun onResponse(
                    call: Call<GetAllFavouritesResponse>,
                    response: Response<GetAllFavouritesResponse>
                ) {
                    if (response.isSuccessful){
                        getFavouritesLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetAllFavouritesResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}