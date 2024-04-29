package com.example.finalproject.ui.profile.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.profile.models.ChangeProfileImageResponse
import com.example.finalproject.ui.profile.models.DeleteProfileImageResponse
import com.example.finalproject.ui.profile.models.EditProfileResponse
import com.example.finalproject.ui.profile.request.EditProfileRequest
import okhttp3.MultipartBody
import retrofit2.Call

class EditProfileRepository(
    private val apiService: ApiService
) {
    fun editProfile(
        token: String,
        firstName: String,
        gender: String,
        username: String,
        lastName: String,
        phone: String
    ): Call<EditProfileResponse> {
        val data = EditProfileRequest(firstName, gender, username, lastName, phone)
        return apiService.editProfile(token, data)
    }

    fun deleteProfileImage(
        token: String
    ): Call<DeleteProfileImageResponse> {
        return apiService.deleteProfileImage(token)
    }


    fun changeImage(
        token: String,
        image: MultipartBody.Part
    ):Call<ChangeProfileImageResponse> {
        return apiService.changeImage(token, image)
    }
}