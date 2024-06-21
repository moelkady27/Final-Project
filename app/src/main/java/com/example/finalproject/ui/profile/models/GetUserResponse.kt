package com.example.finalproject.ui.profile.models


data class GetUserResponse(
    val approvedCount: Int,
    val pendingCount: Int,
    val soldCount: Int,
    val status: String,
    val user: User
)