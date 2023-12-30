package com.example.finalproject.ui.complete_register.models

data class UserComplete(
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val image: ImageComplete,
    val isVerified: Boolean,
    val lastName: String,
    val phone: String,
    val role: String,
    val updatedAt: String,
    val username: String
)