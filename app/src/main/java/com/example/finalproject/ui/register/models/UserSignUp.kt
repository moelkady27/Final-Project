package com.example.finalproject.ui.register.models

data class UserSignUp(
    val _id: String,
    val createdAt: String,
    val email: String,
    val isVerified: Boolean,
    val role: String,
    val tokens: List<TokenSignUp>,
    val updatedAt: String,
    val username: String,
    val wishlist: List<Any>
)