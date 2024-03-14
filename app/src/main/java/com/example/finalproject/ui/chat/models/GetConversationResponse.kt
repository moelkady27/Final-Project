package com.example.finalproject.ui.chat.models

data class GetConversationResponse(
    val messages: MessagesConversation,
    val status: String
)