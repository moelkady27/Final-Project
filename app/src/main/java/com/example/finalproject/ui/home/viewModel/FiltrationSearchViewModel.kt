package com.example.finalproject.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.home.models.FiltrationSearchResponse
import com.example.finalproject.ui.home.repository.FiltrationSearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FiltrationSearchViewModel(
    private val filtrationSearchRepository: FiltrationSearchRepository
) : ViewModel() {

    val filtrationSearchLiveData: MutableLiveData<FiltrationSearchResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var currentPage = 1
    private var isLastPage = false

    fun filtrationSearch(
        token: String,
        minPrice: String, maxPrice: String, rating: String,
        bedroom: String, bathroom: String, neighborhood: String
    ) {
        if (loadingLiveData.value == true || isLastPage) {
            return
        }

        loadingLiveData.value = true
        filtrationSearchRepository.filtrationSearch(
            "Bearer $token", currentPage, minPrice, maxPrice,
            rating, bedroom, bathroom, neighborhood
        )
            .enqueue(object : Callback<FiltrationSearchResponse> {
                override fun onResponse(
                    call: Call<FiltrationSearchResponse>,
                    response: Response<FiltrationSearchResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        val data = response.body()
                        filtrationSearchLiveData.value = data!!
                        currentPage++
                        if (data?.residences?.isEmpty() == true) {
                            isLastPage = true
                        }
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<FiltrationSearchResponse>, t: Throwable) {
                    loadingLiveData.value = false
                    errorLiveData.value = t.message
                }
            })
    }
}