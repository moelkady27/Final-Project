package com.example.finalproject.ui.chat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finalproject.ui.chat.models.ChatUser
import com.example.finalproject.ui.chat.models.MessageConversation

@Database(entities = [ChatUser::class], version = 1)
@TypeConverters(ChatUsersConverters::class)
abstract class ChatUsersDatabase : RoomDatabase() {
    abstract fun chatUsersDao(): ChatUsersDao

    companion object {
        @Volatile
        private var INSTANCE: ChatUsersDatabase? = null

        fun getInstance(context: Context): ChatUsersDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatUsersDatabase::class.java, "chatUsers_database"
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