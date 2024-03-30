package com.example.finalproject.ui.chat.models

data class LastMessage(
    val _id: String,
    val createdAt: String,
    var media: List<Any>,
    var messageContent: String,
    val receiverId: String,
    val senderId: String,
    val updatedAt: String
)