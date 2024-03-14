package com.example.finalproject.ui.chat.models

data class MessageConversation(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val message: Message,
    val receiverId: String,
    val senderId: String,
    val updatedAt: String
)