package com.example.finalproject.ui.residence_details.models

data class ReviewX(
    val __v: Int,
    val _id: String,
    val comment: String,
    val createdAt: String,
    val likedBy: List<Any>,
    val rating: Int,
    val residenceId: ResidenceIdX,
    val reviewLikes: Int,
    val updatedAt: String,
    val userId: UserIdX
)