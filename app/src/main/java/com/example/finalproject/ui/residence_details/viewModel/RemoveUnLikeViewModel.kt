package com.example.finalproject.ui.residence_details.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.residence_details.models.LikeReviewResponse
import com.example.finalproject.ui.residence_details.repository.RemoveUnLikeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoveUnLikeViewModel(
    private val removeUnLikeRepository: RemoveUnLikeRepository
): ViewModel() {
    val removeUnLikeLiveData: MutableLiveData<LikeReviewResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun removeUnLike(token: String, reviewId: String) {
        removeUnLikeRepository.removeUnLike("Bearer $token", reviewId)
            .enqueue(object : Callback<LikeReviewResponse> {
                override fun onResponse(
                    call: Call<LikeReviewResponse>,
                    response: Response<LikeReviewResponse>
                ) {
                    if (response.isSuccessful){
                        removeUnLikeLiveData.value = response.body()
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