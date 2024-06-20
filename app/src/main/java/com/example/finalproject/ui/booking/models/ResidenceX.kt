package com.example.finalproject.ui.booking.models

data class ResidenceX(
    val _id: String,
    val avgRating: Int,
    val buyerId: BuyerIdX,
    val category: String,
    val images: List<ImageXXX>,
    val isCompleted: Boolean,
    val isLiked: Boolean,
    val isSold: Boolean,
    val location: LocationX,
    val ownerId: OwnerIdX,
    val reviews: List<Review>,
    val title: String,
    val type: String
)