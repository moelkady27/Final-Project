package com.example.finalproject.ui.add_listing.models

data class ResidenceX(
    val Id: String,
    val KitchenAbvGr: Int,
    val _id: String,
    val bedroomAbvGr: Int,
    val category: String,
    val hasBasement: Boolean,
    val hasFireplace: Boolean,
    val hasGarage: Boolean,
    val images: List<ImageX>,
    val isCompleted: Boolean,
    val isSold: Boolean,
    val location: LocationXX,
    val neighborhood: String,
    val ownerId: String,
    val paymentPeriod: String,
    val reviews: List<Any>,
    val salePrice: Int,
    val title: String,
    val totRmsAbvGrd: Int,
    val totalarea: Int,
    val totalbaths: Int,
    val totalporchsf: Int,
    val type: String
)