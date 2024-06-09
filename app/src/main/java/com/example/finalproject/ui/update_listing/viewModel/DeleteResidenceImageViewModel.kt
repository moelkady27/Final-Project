package com.example.finalproject.ui.update_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.update_listing.models.DeleteResidenceImageResponse
import com.example.finalproject.ui.update_listing.repository.DeleteResidenceImageRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteResidenceImageViewModel(
    private val deleteResidenceImageRepository: DeleteResidenceImageRepository
): ViewModel() {

    val deleteResidenceImageResponseLiveData: MutableLiveData<DeleteResidenceImageResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun deleteImage(token: String, imageId: String){
        deleteResidenceImageRepository.deleteImage(token, imageId)
            .enqueue(object : Callback<DeleteResidenceImageResponse> {
                override fun onResponse(
                    call: Call<DeleteResidenceImageResponse>,
                    response: Response<DeleteResidenceImageResponse>
                ) {
                    if (response.isSuccessful){
                        deleteResidenceImageResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<DeleteResidenceImageResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}