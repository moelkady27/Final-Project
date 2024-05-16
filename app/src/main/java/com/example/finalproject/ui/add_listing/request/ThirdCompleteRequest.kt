package com.example.finalproject.ui.add_listing.request

data class ThirdCompleteRequest(
    val hasGarage: String,
    val garageType: String,
    val garageQual: String,
    val garageCars: String,
    val garageFinish: String,
    val hasBasement: String,
    val bsmtUnfSF: String,
    val bsmtExposure: String,
    val bsmtFinType1: String,
    val bsmtQual: String,
    val bsmtCond: String,
    val hasFireplace: String,
    val fireplaces: String,
    val fireplaceQu: String,
    val bedroomAbvGr: String,
    val totalbaths: String,
    val KitchenAbvGr: String,
    val kitchenQual: String,
    val totRmsAbvGrd: String
)
