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
): ViewModel() {
    val homeFeaturedEstatesLiveData: MutableLiveData<GetAllResidencesResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getFeaturedEstates(token: String){
        homeFeaturedEstatesRepository.getFeaturedEstates("Bearer $token")
            .enqueue(object : Callback<GetAllResidencesResponse>{
                override fun onResponse(
                    call: Call<GetAllResidencesResponse>,
                    response: Response<GetAllResidencesResponse>
                ) {
                    if (response.isSuccessful){
                        homeFeaturedEstatesLiveData.value = response.body()
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
