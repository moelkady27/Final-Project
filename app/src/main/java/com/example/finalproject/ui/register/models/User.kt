package com.example.finalproject.ui.register.models

data class User(
    val __v: Int,
    val _id: String,
    val counter: Int,
    val createdAt: String,
    val email: String,
    val isVerified: Boolean,
    val otp: Int,
    val password: String,
    val role: String,
    val updatedAt: String,
    val userName: String,
    val wishlist: List<Any>
)