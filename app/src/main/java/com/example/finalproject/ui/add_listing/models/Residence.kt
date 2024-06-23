package com.example.finalproject.ui.add_listing.models

data class Residence(
    val Id: String,
    val _id: String,
    val category: String,
    val hasBasement: Boolean,
    val hasFireplace: Boolean,
    val hasGarage: Boolean,
    val images: List<Any>,
    val isCompleted: Boolean,
    val isSold: Boolean,
    val location: LocationX,
    val ownerId: String,
    val reviews: List<Any>,
    val title: String,
    val totalarea: Int,
    val totalbaths: Int,
    val totalporchsf: Int,
    val type: String
)