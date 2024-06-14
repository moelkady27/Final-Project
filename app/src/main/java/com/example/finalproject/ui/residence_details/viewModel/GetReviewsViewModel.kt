package com.example.finalproject.ui.residence_details.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.residence_details.models.GetReviewsResponse
import com.example.finalproject.ui.residence_details.repository.GetReviewsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetReviewsViewModel(
    private val getReviewsRepository: GetReviewsRepository
) : ViewModel() {

    val getReviewsResponseLiveData: MutableLiveData<GetReviewsResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getReviews(token: String, residenceId: String) {
        getReviewsRepository.getReviews("Bearer $token", residenceId)
            .enqueue(object : Callback<GetReviewsResponse> {
                override fun onResponse(
                    call: Call<GetReviewsResponse>,
                    response: Response<GetReviewsResponse>
                ) {
                    if (response.isSuccessful) {
                        getReviewsResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetReviewsResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}
