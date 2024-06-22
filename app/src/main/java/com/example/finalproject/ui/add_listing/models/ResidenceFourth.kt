package com.example.finalproject.ui.add_listing.models

data class ResidenceFourth(
    val _id: String,
    val Id: String,
    val category: String,
    val hasBasement: Boolean,
    val hasFireplace: Boolean,
    val hasGarage: Boolean,
    val images: List<Any>,
    val isCompleted: Boolean,
    val isSold: Boolean,
    val location: LocationResidenceFourth,
    val ownerId: OwnerIdFourth,
    val reviews: List<Any>,
    val title: String,
    val totalarea: Int,
    val totalbaths: Int,
    val totalporchsf: Int,
    val type: String
)