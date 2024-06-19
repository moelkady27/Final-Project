package com.example.finalproject.ui.residence_details.models

data class LikeReviewResponse(
    val likes: Int,
    val message: String,
    val reviewId: String,
    val status: String,
    val unLikes: Int
)