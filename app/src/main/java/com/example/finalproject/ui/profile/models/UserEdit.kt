package com.example.finalproject.ui.profile.models

data class UserEdit(
    val _id: String,
    val createdAt: String,
    val email: String,
    val fullName: String,
    val firstName: String,
    val gender: String,
    val image: ImageEdit,
    val isVerified: Boolean,
    val lastName: String,
    val location: LocationEdit,
    val phone: String,
    val role: String,
    val updatedAt: String,
    val username: String
)