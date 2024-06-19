package com.example.finalproject.ui.residence_details.models

data class Review(
    val __v: Int,
    val _id: String,
    val comment: String,
    val createdAt: String,
    var likedBy: List<LikedBy>,
    var likes: Int,
    val rating: Int,
    val residenceId: ResidenceId,
    val unLikedBy: List<Any>,
    var unLikes: Int,
    val updatedAt: String,
    val userId: UserId
)