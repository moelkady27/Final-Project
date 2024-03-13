package com.example.finalproject.ui.chat.models

data class MessageChatting(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val message: Messages,
    val receiverId: String,
    val senderId: String,
    val updatedAt: String
)