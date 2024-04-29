package com.example.finalproject.ui.setting.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.setting.models.DeleteAccountResponse
import com.example.finalproject.ui.setting.request.DeleteAccountRequest
import retrofit2.Call

class DeleteAccountRepository(
    private val apiService: ApiService
) {
    fun deleteAccount(
        token: String,
        password: String
    ): Call<DeleteAccountResponse> {
        val data = DeleteAccountRequest(password)
        return apiService.deleteAccount(token, data)
    }
}