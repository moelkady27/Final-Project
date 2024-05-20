package com.example.finalproject.ui.favourite.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.favourite.models.DeleteAllFavouriteResponse
import com.example.finalproject.ui.favourite.repository.DeleteAllFavouriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAllFavouriteViewModel(
    private val deleteAllFavouriteRepository: DeleteAllFavouriteRepository
): ViewModel() {
    val deleteAllFavouriteLiveData: MutableLiveData<DeleteAllFavouriteResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun deleteAllFavourite(token: String){
        deleteAllFavouriteRepository.deleteAllFavourite("Bearer $token")
            .enqueue(object : Callback<DeleteAllFavouriteResponse> {
                override fun onResponse(
                    call: Call<DeleteAllFavouriteResponse>,
                    response: Response<DeleteAllFavouriteResponse>
                ) {
                    if (response.isSuccessful){
                        deleteAllFavouriteLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<DeleteAllFavouriteResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}