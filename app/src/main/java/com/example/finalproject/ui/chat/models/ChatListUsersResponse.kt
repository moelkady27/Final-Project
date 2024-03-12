package com.example.finalproject.ui.chat.models

data class ChatListUsersResponse(
    val chatUsers: List<ChatUser>,
    val status: String
)