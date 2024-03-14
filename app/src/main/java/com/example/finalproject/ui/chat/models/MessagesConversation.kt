package com.example.finalproject.ui.chat.models

data class MessagesConversation(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val lastMessage: String,
    val messages: List<MessageConversation>,
    val participants: List<String>,
    val updatedAt: String
)