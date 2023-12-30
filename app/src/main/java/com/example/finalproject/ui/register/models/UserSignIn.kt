package com.example.finalproject.ui.register.models

data class UserSignIn(
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val image: ImageSignIn,
    val isVerified: Boolean,
    val lastName: String,
    val location: LocationSignIn,
    val phone: String,
    val role: String,
    val updatedAt: String,
    val username: String
)