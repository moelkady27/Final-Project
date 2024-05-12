package com.example.finalproject.ui.profile.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalproject.ui.profile.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user WHERE _id = :id")
    suspend fun getUser(id: String): User
}