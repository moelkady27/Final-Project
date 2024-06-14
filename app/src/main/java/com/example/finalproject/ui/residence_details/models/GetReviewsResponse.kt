package com.example.finalproject.ui.residence_details.models

data class GetReviewsResponse(
    val count: Int,
    val reviews: List<Review>,
    val status: String
)