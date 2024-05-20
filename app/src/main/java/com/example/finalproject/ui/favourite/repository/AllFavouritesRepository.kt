package com.example.finalproject.ui.favourite.repository

import com.example.finalproject.retrofit.ApiService
import com.example.finalproject.ui.favourite.models.AddToFavouritesResponse
import com.example.finalproject.ui.favourite.models.GetAllFavouritesResponse
import com.example.finalproject.ui.home.models.GetAllResidencesResponse
import retrofit2.Call

class AllFavouritesRepository(
    private val apiService: ApiService
) {
    fun getFavourites(
        token: String,
    ): Call<GetAllFavouritesResponse> {
        return apiService.getFavorites(token)
    }
}
