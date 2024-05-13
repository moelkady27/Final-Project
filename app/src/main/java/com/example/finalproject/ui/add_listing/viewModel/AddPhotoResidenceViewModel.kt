package com.example.finalproject.ui.add_listing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.add_listing.models.UploadPhotoResidenceResponse
import com.example.finalproject.ui.add_listing.repository.AddPhotoResidenceRepository
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPhotoResidenceViewModel(
    private val addPhotoResidenceRepository: AddPhotoResidenceRepository
): ViewModel() {

    val uploadPhotoResponseLiveData: MutableLiveData<UploadPhotoResidenceResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun uploadResidencePhoto(token: String, residenceId: String, image: MultipartBody.Part) {
        addPhotoResidenceRepository.uploadResidencePhoto("Bearer $token", residenceId, image)
            .enqueue(object : Callback<UploadPhotoResidenceResponse> {
                override fun onResponse(
                    call: Call<UploadPhotoResidenceResponse>,
                    response: Response<UploadPhotoResidenceResponse>
                ) {
                    if (response.isSuccessful) {
                        uploadPhotoResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<UploadPhotoResidenceResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}