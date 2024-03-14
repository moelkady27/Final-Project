package com.example.finalproject.ui.chat.models.lol

data class Message(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val message: MessageX,
    val receiverId: String,
    val senderId: String,
    val updatedAt: String
)