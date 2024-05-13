package com.example.finalproject.ui.add_listing.models

data class Image(
    val _id: String,
    val public_id: String,
    val url: String,
    val isAddButton: Boolean = false
)