package com.example.finalproject.ui.favourite.models

data class Wishlist(
    val _id: String,
    val category: String,
    val images: List<Image>,
    val isLiked: Boolean,
    val location: String,
    val paymentPeriod: String,
    val rating: Int,
    val salePrice: Int,
    val title: String
)