package com.example.finalproject.ui.favourite.models

data class GetAllFavouritesResponse(
    val count: Int,
    val status: String,
    val wishlist: List<Wishlist>
)