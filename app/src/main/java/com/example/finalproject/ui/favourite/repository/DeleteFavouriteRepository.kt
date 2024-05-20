package com.example.finalproject.ui.favourite.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.favourite.models.DeleteFavouriteResponse
import retrofit2.Call

class DeleteFavouriteRepository(
    private val apiService: ApiService
) {
    fun deleteFavourite(
        token: String,
        residenceId: String
    ): Call<DeleteFavouriteResponse> {
        return apiService.deleteFavorite(token, residenceId)
    }
}
