package com.example.finalproject.ui.booking.models

data class Review(
    val __v: Int,
    val _id: String,
    val comment: String,
    val createdAt: String,
    val likedBy: List<String>,
    val likes: Int,
    val rating: Int,
    val residenceId: String,
    val unLikedBy: List<String>,
    val unLikes: Int,
    val updatedAt: String,
    val userId: UserId
)