package com.example.finalproject.ui.register.models

data class VerificationCodeSignUpResponse(
    val status: String,
    val token: String,
    val user: UserVerification
)