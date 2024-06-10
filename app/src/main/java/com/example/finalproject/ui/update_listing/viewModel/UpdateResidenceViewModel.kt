package com.example.finalproject.ui.update_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.repository.UpdateResidenceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateResidenceViewModel(
    private val updateResidenceRepository: UpdateResidenceRepository
): ViewModel() {

    val updateResidenceResponseLiveData: MutableLiveData<UpdateResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun updateResidence(token: String, residenceId: String, title: String, type: String, category: String) {
        updateResidenceRepository.updateResidence("Bearer $token", residenceId, title, type, category)
            .enqueue(object: Callback<UpdateResidenceResponse>{
                override fun onResponse(
                    call: Call<UpdateResidenceResponse>,
                    response: Response<UpdateResidenceResponse>
                ) {
                    if (response.isSuccessful) {
                        updateResidenceResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<UpdateResidenceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}