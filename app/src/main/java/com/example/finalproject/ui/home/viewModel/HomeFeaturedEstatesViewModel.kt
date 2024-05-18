package com.example.finalproject.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.home.models.GetAllResidencesResponse
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFeaturedEstatesViewModel(
    private val homeFeaturedEstatesRepository: HomeFeaturedEstatesRepository
) : ViewModel() {

    val homeFeaturedEstatesLiveData: MutableLiveData<GetAllResidencesResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var currentPage = 1
    private var isLastPage = false


                                /* Get Featured Estates View All */
    fun getFeaturedEstatesViewAll(token: String) {
        if (loadingLiveData.value == true || isLastPage) {
            return
        }

        loadingLiveData.value = true
        homeFeaturedEstatesRepository.getFeaturedEstates("Bearer $token", currentPage)
            .enqueue(object : Callback<GetAllResidencesResponse> {
                override fun onResponse(
                    call: Call<GetAllResidencesResponse>,
                    response: Response<GetAllResidencesResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        val data = response.body()
                        homeFeaturedEstatesLiveData.value = data!!
                        currentPage++
                        if (data?.residences?.isEmpty() == true) {
                            isLastPage = true
                        }
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetAllResidencesResponse>, t: Throwable) {
                    loadingLiveData.value = false
                    errorLiveData.value = t.message
                }
            })
    }


                                /* Get Featured Estates Home */
    fun getFeaturedEstates(token: String) {
        homeFeaturedEstatesRepository.getFeaturedEstates("Bearer $token", currentPage)
            .enqueue(object : Callback<GetAllResidencesResponse> {
                override fun onResponse(
                    call: Call<GetAllResidencesResponse>,
                    response: Response<GetAllResidencesResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        homeFeaturedEstatesLiveData.value = data!!
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetAllResidencesResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}
