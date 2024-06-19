package com.example.finalproject.ui.residence_details.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.residence_details.models.LikeReviewResponse
import com.example.finalproject.ui.residence_details.repository.UnLikeReviewRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnLikeReviewViewModel(
    private val unLikeReviewRepository: UnLikeReviewRepository
): ViewModel() {
    val unLikeReviewLiveData: MutableLiveData<LikeReviewResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun unLikeReview(token: String, reviewId: String) {
        unLikeReviewRepository.unLikeReview("Bearer $token", reviewId)
            .enqueue(object : Callback<LikeReviewResponse> {
                override fun onResponse(
                    call: Call<LikeReviewResponse>,
                    response: Response<LikeReviewResponse>
                ) {
                    if (response.isSuccessful){
                        unLikeReviewLiveData.value = response.body()
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