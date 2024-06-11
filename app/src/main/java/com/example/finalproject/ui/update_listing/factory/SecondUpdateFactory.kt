package com.example.finalproject.ui.update_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.update_listing.repository.SecondUpdateRepository
import com.example.finalproject.ui.update_listing.viewModel.SecondUpdateViewModel

class SecondUpdateFactory(
    private val secondUpdateRepository: SecondUpdateRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondUpdateViewModel::class.java)){
            return SecondUpdateViewModel(secondUpdateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}