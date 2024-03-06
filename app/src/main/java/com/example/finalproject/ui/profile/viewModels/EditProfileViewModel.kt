package com.example.finalproject.ui.profile.viewModels

import android.media.session.MediaSession.Token
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.complete_register.models.CompleteSignUpResponse
import com.example.finalproject.ui.complete_register.models.UploadPhotoResponse
import com.example.finalproject.ui.profile.models.ChangeProfileImageResponse
import com.example.finalproject.ui.profile.models.DeleteProfileImageResponse
import com.example.finalproject.ui.profile.models.EditProfileResponse
import com.example.finalproject.ui.profile.request.EditProfileRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel: ViewModel() {

    val editProfileResponseLiveData: MutableLiveData<EditProfileResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun editProfile(token: String , firstName: String , gender: String , username: String , lastName: String, phone: String){
        val data = EditProfileRequest(firstName, gender, username, lastName, phone)

        RetrofitClient.instance.editProfile("Bearer $token" , data)
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

    val deleteProfileImageResponseLiveData: MutableLiveData<DeleteProfileImageResponse> = MutableLiveData()

    fun deleteProfileImage(token: String){

        RetrofitClient.instance.deleteProfileImage("Bearer $token")
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

    val changeProfileImageResponseLiveData: MutableLiveData<ChangeProfileImageResponse> = MutableLiveData()

    fun changeImage(token: String, image: MultipartBody.Part) {
        RetrofitClient.instance.changeImage("Bearer $token", image)
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