package com.example.finalproject.ui.profile.request

import com.example.finalproject.ui.profile.models.User

data class EditProfileRequest(
    val firstName: String,
    val gender: String,
    val username: String,
    val lastName: String,
    val phone: String,
)
