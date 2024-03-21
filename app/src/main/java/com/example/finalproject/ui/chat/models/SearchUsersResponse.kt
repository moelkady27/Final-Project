package com.example.finalproject.ui.chat.models

data class SearchUsersResponse(
    val status: String,
    val users: List<User>
)