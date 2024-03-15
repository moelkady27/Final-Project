package com.example.finalproject.ui.register.models

data class SignUpResponse(
    val message: String,
    val status: String,
    val token: String,
    val userId: String
)