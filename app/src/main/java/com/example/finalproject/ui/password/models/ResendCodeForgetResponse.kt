package com.example.finalproject.ui.password.models

data class ResendCodeForgetResponse(
    val email: String,
    val message: String,
    val status: String
)