package com.example.finalproject.ui.chat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finalproject.ui.chat.models.Chat
import com.example.finalproject.ui.chat.models.MessageConversation


@Database(entities = [Chat::class, MessageConversation::class], version = 1)
//@Database(entities = [Chat::class], version = 1)
@TypeConverters(MessageListConverter::class, MediaListConverter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getInstance(context: Context): ChatDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatDatabase::class.java, "chat_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}