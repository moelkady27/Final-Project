package com.example.finalproject.ui.favourite.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.favourite.models.DeleteAllFavouriteResponse
import retrofit2.Call

class DeleteAllFavouriteRepository(
    private val apiService: ApiService
) {
    fun deleteAllFavourite(
        token: String,
    ): Call<DeleteAllFavouriteResponse> {
        return apiService.deleteAllFavorites(token)
    }
}
