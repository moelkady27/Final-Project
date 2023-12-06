package com.example.finalproject.ui.register.models

data class UserComplete(
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val isVerified: Boolean,
    val lastName: String,
    val phone: String,
    val gender: String,
    val role: String,
    val tokens: List<TokenComplete>,
    val updatedAt: String,
    val username: String,
    val wishlist: List<Any>
)