package com.example.finalproject.ui.register.models

data class UserVerification(
    val _id: String,
    val createdAt: String,
    val email: String,
    val image: ImageVerification,
    val isVerified: Boolean,
    val role: String,
    val updatedAt: String,
    val username: String
)