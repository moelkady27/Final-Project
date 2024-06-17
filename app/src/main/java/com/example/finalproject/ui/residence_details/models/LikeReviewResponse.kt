package com.example.finalproject.ui.residence_details.models

data class LikeReviewResponse(
    val message: String,
    val reviewId: String,
    val reviewLikes: Int,
    val status: String
)