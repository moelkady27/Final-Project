package com.example.finalproject.ui.residence_details.models

data class ResidenceIdX(
    val _id: String,
    val avgRating: Int,
    val category: String,
    val images: List<ImageXX>,
    val isLiked: Boolean,
    val location: LocationX,
    val paymentPeriod: String,
    val salePrice: Int,
    val title: String,
    val type: String
)