package com.example.finalproject.ui.favourite.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.favourite.models.DeleteFavouriteResponse
import com.example.finalproject.ui.favourite.repository.DeleteFavouriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteFavouriteViewModel(
    private val deleteFavouritesRepository: DeleteFavouriteRepository
): ViewModel() {
    val deleteFavouriteLiveData: MutableLiveData<DeleteFavouriteResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun deleteFavourite(token: String, residenceId: String){
        deleteFavouritesRepository.deleteFavourite("Bearer $token", residenceId)
            .enqueue(object : Callback<DeleteFavouriteResponse> {
                override fun onResponse(
                    call: Call<DeleteFavouriteResponse>,
                    response: Response<DeleteFavouriteResponse>
                ) {
                    if (response.isSuccessful){
                        deleteFavouriteLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<DeleteFavouriteResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}