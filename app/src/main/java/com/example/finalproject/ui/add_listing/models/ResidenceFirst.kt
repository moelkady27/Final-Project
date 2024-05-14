package com.example.finalproject.ui.add_listing.models

data class ResidenceFirst(
    val _id: String,
    val category: String,
    val hasBasement: Boolean,
    val hasFireplace: Boolean,
    val hasGarage: Boolean,
    val images: List<Any>,
    val isCompleted: Boolean,
    val isSold: Boolean,
    val location: LocationFirst,
    val neighborhood: String,
    val ownerId: String,
    val paymentPeriod: String,
    val reviews: List<Any>,
    val salePrice: Int,
    val title: String,
    val totalarea: Int,
    val totalbaths: Int,
    val totalporchsf: Int,
    val type: String
)