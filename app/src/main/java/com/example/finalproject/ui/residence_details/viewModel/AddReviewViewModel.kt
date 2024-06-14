package com.example.finalproject.ui.residence_details.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.residence_details.models.AddReviewResponse
import com.example.finalproject.ui.residence_details.repository.AddReviewRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewViewModel(
    private val addReviewRepository: AddReviewRepository
): ViewModel() {
    val addReviewLiveData: MutableLiveData<AddReviewResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun addReview(
        token: String, residenceId: String, rating: String, comment: String
    ) {
        addReviewRepository.addReview(
            "Bearer $token", residenceId, rating, comment
        )
            .enqueue(object : Callback<AddReviewResponse> {
                override fun onResponse(
                    call: Call<AddReviewResponse>,
                    response: Response<AddReviewResponse>
                ) {
                    if (response.isSuccessful){
                        addReviewLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}