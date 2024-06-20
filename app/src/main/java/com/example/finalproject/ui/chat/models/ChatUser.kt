package com.example.finalproject.ui.chat.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_list_users")
data class ChatUser(
    @PrimaryKey
    val _id: String,
    val fullName: String?,
    val image: Image,
    val lastMessage: LastMessage,
    val username: String
)