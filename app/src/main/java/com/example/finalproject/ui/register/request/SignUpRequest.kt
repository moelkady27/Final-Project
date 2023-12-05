package com.example.finalproject.ui.register.request

data class SignUpRequest(
    val userName: String,
    val email: String,
    val password: String,
    val confirmPass: String
)