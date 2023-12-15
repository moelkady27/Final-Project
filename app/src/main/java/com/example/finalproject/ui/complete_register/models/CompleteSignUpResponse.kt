package com.example.finalproject.ui.complete_register.models

data class CompleteSignUpResponse(
    val status: String,
    val token: String,
    val user: UserComplete
)