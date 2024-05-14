package com.example.finalproject.ui.add_listing.request

data class FirstCompleteRequest(
    val neighborhood: String,
    val mszoning: String,
    val saleCondition: String,
    val moSold: String,
    val salePrice: String,
    val paymentPeriod: String,
    val saleType: String,
    val utilities: List<String>,
    val lotShape: String,
    val electrical: String,
    val foundation: String,
    val bldgType: String
)