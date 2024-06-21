package com.example.finalproject.ui.home.models

data class ResidenceXX(
    val _id: String,
    val avgRating: Int,
    val bedroomAbvGr: Int,
    val category: String,
    val images: List<Image>,
    val isLiked: Boolean,
    val location: LocationX,
    val neighborhood: String,
    val reviews: List<Any>,
    val salePrice: Double,
    val title: String,
    val totalbaths: Double
)