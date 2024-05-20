package com.example.finalproject.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.home.models.GetNearestResidencesResponse
import com.example.finalproject.ui.home.repository.HomePopularEstatesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePopularEstatesViewModel(
    private val homePopularEstatesRepository: HomePopularEstatesRepository
) : ViewModel() {

    val homePopularEstatesLiveData: MutableLiveData<GetNearestResidencesResponse> =
        MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var currentPage = 1
    private var isLastPage = false


                                /* Get Popular Estates Home */
    fun getPopularEstates(token: String) {
        homePopularEstatesRepository.getNearestEstates("Bearer $token", currentPage)
            .enqueue(object : Callback<GetNearestResidencesResponse> {
                override fun onResponse(
                    call: Call<GetNearestResidencesResponse>,
                    response: Response<GetNearestResidencesResponse>
                ) {
                    if (response.isSuccessful) {
                        homePopularEstatesLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetNearestResidencesResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }


                                /* Get Popular Estates View All */
    fun getPopularEstatesViewAll(token: String) {
        if (loadingLiveData.value == true || isLastPage) {
            return
        }

        loadingLiveData.value = true
        homePopularEstatesRepository.getNearestEstates("Bearer $token", currentPage)
            .enqueue(object : Callback<GetNearestResidencesResponse> {
                override fun onResponse(
                    call: Call<GetNearestResidencesResponse>,
                    response: Response<GetNearestResidencesResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        val data = response.body()
                        homePopularEstatesLiveData.value = data!!
                        currentPage++
                        if (data?.residences?.isEmpty() == true) {
                            isLastPage = true
                        }
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetNearestResidencesResponse>, t: Throwable) {
                    loadingLiveData.value = false
                    errorLiveData.value = t.message
                }
            })
    }

}