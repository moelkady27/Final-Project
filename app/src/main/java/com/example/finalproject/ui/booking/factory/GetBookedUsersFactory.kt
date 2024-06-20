package com.example.finalproject.ui.booking.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.booking.repository.GetBookedUsersRepository
import com.example.finalproject.ui.booking.viewModel.GetBookedUsersViewModel

class GetBookedUsersFactory(
    private val getBookedUsersRepository: GetBookedUsersRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetBookedUsersViewModel::class.java)){
            return GetBookedUsersViewModel(getBookedUsersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}