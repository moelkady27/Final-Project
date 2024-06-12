package com.example.finalproject.ui.update_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.update_listing.repository.FourthUpdateRepository
import com.example.finalproject.ui.update_listing.viewModel.FourthUpdateViewModel

class FourthUpdateFactory(
    private val fourthUpdateRepository: FourthUpdateRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FourthUpdateViewModel::class.java)){
            return FourthUpdateViewModel(fourthUpdateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}