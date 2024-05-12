package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.SetLocationResidenceResponse
import com.example.finalproject.ui.add_listing.repository.SetLocationResidenceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetLocationResidenceViewModel(
    private val setLocationResidenceRepository: SetLocationResidenceRepository
):ViewModel() {

    val locationResponseLiveData: MutableLiveData<SetLocationResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun setLocationResidence(token: String, residenceId: String, longitude: Double, latitude: Double) {
        setLocationResidenceRepository.setLocationRe("Bearer $token", residenceId, longitude, latitude)
            .enqueue(object : Callback<SetLocationResidenceResponse>{
                override fun onResponse(
                    call: Call<SetLocationResidenceResponse>,
                    response: Response<SetLocationResidenceResponse>
                ) {
                    if (response.isSuccessful) {
                        locationResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<SetLocationResidenceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }
}