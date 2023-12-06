package com.example.finalproject.ui.register.models

data class CompleteSignUpResponse(
    val status: String,
    val token: String,
    val user: UserComplete
)