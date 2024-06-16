package com.example.finalproject.ui.residence_details.models

data class Review(
    val __v: Int,
    val _id: String,
    val comment: String,
    val createdAt: String,
    val likedBy: List<LikedBy>,
    val rating: Int,
    val residenceId: ResidenceId,
    val reviewLikes: Int,
    val unLikes: Int,
    val updatedAt: String,
    val userId: UserId
)