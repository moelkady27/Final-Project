package com.example.finalproject.ui.profile.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val fullName: String,
    val gender: String,
    val image: Image,
    val isVerified: Boolean,
    val lastName: String,
    val location: Location,
    val phone: String,
    val role: String,
    val updatedAt: String,
    val username: String
)