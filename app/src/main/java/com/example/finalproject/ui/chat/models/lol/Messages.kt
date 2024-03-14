package com.example.finalproject.ui.chat.models.lol

data class Messages(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val lastMessage: String,
    val messages: List<Message>,
    val participants: List<String>,
    val updatedAt: String
)