package com.example.finalproject.ui.favourite.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.favourite.models.AddToFavouritesResponse
import com.example.finalproject.ui.home.models.GetAllResidencesResponse
import retrofit2.Call

class AddToFavouritesRepository(
    private val apiService: ApiService
) {
    fun addToFavourites(
        token: String,
        residenceId: String
    ): Call<AddToFavouritesResponse> {
        return apiService.addToFavorites(token, residenceId)
    }
}
