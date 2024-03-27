package com.example.finalproject.ui.chat.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Chat")
data class Chat(
    @PrimaryKey
    val _id: String,
    val messages: List<MessageConversation>
)