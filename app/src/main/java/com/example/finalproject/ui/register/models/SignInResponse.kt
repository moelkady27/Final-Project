package com.example.finalproject.ui.register.models

data class SignInResponse(
    val status: String,
    val token: String,
    val user: User
)