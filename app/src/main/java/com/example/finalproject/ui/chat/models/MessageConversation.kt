package com.example.finalproject.ui.chat.models

import android.media.browse.MediaBrowser
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MessageConversation")
data class MessageConversation(
    @PrimaryKey
    val _id: String,
    val createdAt: String,
//    val media: List<Any>,
    val media: List<MediaBrowser.MediaItem>, // Update the type to MediaBrowser.MediaItem

    val messageContent: String,
    val receiverId: String,
    val senderId: String,
    val updatedAt: String
)