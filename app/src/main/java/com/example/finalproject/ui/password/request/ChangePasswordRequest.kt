package com.example.finalproject.ui.password.request

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmPass: String
)
