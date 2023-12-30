package com.example.finalproject.ui.complete_register.models

data class UserLocation(
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val image: ImageLocation,
    val isVerified: Boolean,
    val lastName: String,
    val location: LocationDetails,
    val phone: String,
    val role: String,
    val updatedAt: String,
    val username: String
)