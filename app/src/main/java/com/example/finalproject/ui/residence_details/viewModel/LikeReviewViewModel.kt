package com.example.finalproject.ui.residence_details.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.residence_details.models.LikeReviewResponse
import com.example.finalproject.ui.residence_details.repository.LikeReviewRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeReviewViewModel(
    private val likeReviewRepository: LikeReviewRepository
): ViewModel() {
    val likeReviewLiveData: MutableLiveData<LikeReviewResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun likeReview( token: String, reviewId: String ) {
        likeReviewRepository.likeReview("Bearer $token", reviewId)
            .enqueue(object : Callback<LikeReviewResponse> {
                override fun onResponse(
                    call: Call<LikeReviewResponse>,
                    response: Response<LikeReviewResponse>
                ) {
                    if (response.isSuccessful){
                        likeReviewLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<LikeReviewResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}