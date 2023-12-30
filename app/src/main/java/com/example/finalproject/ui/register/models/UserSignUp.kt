package com.example.finalproject.ui.register.models

data class UserSignUp(
    val _id: String,
    val createdAt: String,
    val email: String,
    val image: ImageSignUp,
    val isVerified: Boolean,
    val role: String,
    val updatedAt: String,
    val username: String
)