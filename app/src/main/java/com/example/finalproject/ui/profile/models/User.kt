package com.example.finalproject.ui.profile.models

data class User(
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val image: Image,
    val isVerified: Boolean,
    val lastName: String,
    val location: Location,
    val phone: String,
    val role: String,
    val updatedAt: String,
    val username: String
)