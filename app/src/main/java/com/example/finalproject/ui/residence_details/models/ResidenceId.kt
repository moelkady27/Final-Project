package com.example.finalproject.ui.residence_details.models

data class ResidenceId(
    val _id: String,
    val avgRating: Int,
    val category: String,
    val images: List<Image>,
    val isLiked: Boolean,
    val location: Location,
    val paymentPeriod: String,
    val salePrice: Int,
    val title: String,
    val type: String
)