package com.example.finalproject.ui.recommendation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.recommendation.models.GetRecommendedEstatesResponse
import com.example.finalproject.ui.recommendation.repository.RecommendationRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationViewModel(
    private val recommendationRepository: RecommendationRepository
): ViewModel() {

    val getRecommendedEstatesResponseLiveData: MutableLiveData<GetRecommendedEstatesResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getRecommendation(token: String, residenceId: String) {
        recommendationRepository.getRecommendation("Bearer $token", residenceId)
            .enqueue(object : Callback<GetRecommendedEstatesResponse>{
                override fun onResponse(
                    call: Call<GetRecommendedEstatesResponse>,
                    response: Response<GetRecommendedEstatesResponse>
                ) {
                    if (response.isSuccessful){
                        getRecommendedEstatesResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody().toString()
                    }
                }

                override fun onFailure(call: Call<GetRecommendedEstatesResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}