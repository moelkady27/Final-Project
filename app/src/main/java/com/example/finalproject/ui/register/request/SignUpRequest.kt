package com.example.finalproject.ui.register.request

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmPass: String
)