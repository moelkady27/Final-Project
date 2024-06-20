package com.example.finalproject.ui.booking.models

data class Residence(
    val _id: String,
    val avgRating: Int,
    val bookedBy: List<Any>,
    val buyerId: BuyerId,
    val category: String,
    val isLiked: Boolean,
    val isSold: Boolean,
    val ownerId: OwnerId,
    val salePrice: Int,
    val title: String,
    val updatedAt: String
)