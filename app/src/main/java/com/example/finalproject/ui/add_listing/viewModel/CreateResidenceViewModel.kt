package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.CreateResidenceResponse
import com.example.finalproject.ui.add_listing.repository.CreateResidenceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateResidenceViewModel(
    private val createResidenceRepository: CreateResidenceRepository
): ViewModel() {
    val createResidenceLiveData: MutableLiveData<CreateResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun createResidence(token: String, title: String, type: String, category: String) {
        createResidenceRepository.createResidence("Bearer $token", title, type, category)
            .enqueue(object : Callback<CreateResidenceResponse> {
                override fun onResponse(
                    call: Call<CreateResidenceResponse>,
                    response: Response<CreateResidenceResponse>
                ) {
                    if (response.isSuccessful) {
                        createResidenceLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<CreateResidenceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}