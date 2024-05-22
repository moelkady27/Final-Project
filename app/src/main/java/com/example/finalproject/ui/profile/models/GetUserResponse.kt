package com.example.finalproject.ui.profile.models

data class GetUserResponse(
    val status: String,
    val soldCount: Int,
    val pendingCount: Int,
    val approvedCount: Int,
    val user: User,
    val wishList: List<Any>
)