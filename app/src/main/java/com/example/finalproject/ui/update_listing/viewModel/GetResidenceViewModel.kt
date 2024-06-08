package com.example.finalproject.ui.update_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.update_listing.models.GetResidenceResponse
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetResidenceViewModel(
    private val getResidenceRepository: GetResidenceRepository
):ViewModel() {

    val getResidenceResponseLiveData: MutableLiveData<GetResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getResidence(token: String, residenceId: String){
        getResidenceRepository.getResidence("Bearer $token", residenceId)
            .enqueue(object : Callback<GetResidenceResponse>{
                override fun onResponse(
                    call: Call<GetResidenceResponse>,
                    response: Response<GetResidenceResponse>
                ) {
                    if (response.isSuccessful){
                        getResidenceResponseLiveData.value = response.body()
                    }
                    else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<GetResidenceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}