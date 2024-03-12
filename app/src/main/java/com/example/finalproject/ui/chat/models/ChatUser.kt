package com.example.finalproject.ui.chat.models

data class ChatUser(
    val _id: String,
    val fullName: String,
    val image: Image,
    val lastMessage: LastMessage,
    val username: String
)