package com.example.finalproject.ui.chat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalproject.ui.chat.models.Chat
import com.example.finalproject.ui.chat.models.MessageConversation

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat")
    suspend fun getChats(): List<Chat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)

    @Query("SELECT * FROM Chat WHERE _id = :chatId")
    suspend fun getChatById(chatId: String): Chat?

    @Query("SELECT * FROM MessageConversation WHERE senderId = :senderId AND receiverId = :receiverId")
    suspend fun getMessagesBySenderId(senderId: String, receiverId: String): List<MessageConversation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageConversation>)

    @Query("DELETE FROM chat")
    suspend fun deleteAllChats()

}