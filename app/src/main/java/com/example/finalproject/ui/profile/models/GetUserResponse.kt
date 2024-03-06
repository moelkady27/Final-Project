package com.example.finalproject.ui.profile.models

data class GetUserResponse(
    val status: String,
    val user: User,
    val wishList: List<Any>
)