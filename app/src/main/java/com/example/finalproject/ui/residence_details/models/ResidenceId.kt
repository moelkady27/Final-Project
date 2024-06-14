package com.example.finalproject.ui.residence_details.models

data class ResidenceId(
    val _id: String,
    val images: List<Image>,
    val isLiked: Boolean,
    val location: Location,
    val title: String
)