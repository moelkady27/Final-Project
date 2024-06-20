package com.example.finalproject.ui.booking.models

data class GetBookedUsersResponse(
    val bookedBy: List<BookedBy>,
    val residenceId: String,
    val status: String
)