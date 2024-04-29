package com.example.finalproject.ui.profile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.ui.profile.models.ChangeProfileImageResponse
import com.example.finalproject.ui.profile.models.DeleteProfileImageResponse
import com.example.finalproject.ui.profile.models.EditProfileResponse
import com.example.finalproject.ui.profile.repository.EditProfileRepository
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(
    private val editProfileRepository: EditProfileRepository
): ViewModel() {

    val editProfileResponseLiveData: MutableLiveData<EditProfileResponse> = MutableLiveData()
    val deleteProfileImageResponseLiveData: MutableLiveData<DeleteProfileImageResponse> = MutableLiveData()
    val changeProfileImageResponseLiveData: MutableLiveData<ChangeProfileImageResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun editProfile(token: String , firstName: String , gender: String , username: String , lastName: String, phone: String){
        editProfileRepository.editProfile("Bearer $token" , firstName, gender, username, lastName, phone)
            .enqueue(object : Callback<EditProfileResponse>{
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful){
                        editProfileResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }

    fun deleteProfileImage(token: String){
        editProfileRepository.deleteProfileImage("Bearer $token")
            .enqueue(object : Callback<DeleteProfileImageResponse>{
                override fun onResponse(
                    call: Call<DeleteProfileImageResponse>,
                    response: Response<DeleteProfileImageResponse>
                ) {
                    if (response.isSuccessful){
                        deleteProfileImageResponseLiveData.value = response.body()
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<DeleteProfileImageResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }

    fun changeImage(token: String, image: MultipartBody.Part) {
        editProfileRepository.changeImage("Bearer $token", image)
            .enqueue(object : Callback<ChangeProfileImageResponse> {
                override fun onResponse(
                    call: Call<ChangeProfileImageResponse>,
                    response: Response<ChangeProfileImageResponse>
                ) {
                    if (response.isSuccessful) {
                        changeProfileImageResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ChangeProfileImageResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }
}