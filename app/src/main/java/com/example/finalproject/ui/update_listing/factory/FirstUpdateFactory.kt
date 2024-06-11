package com.example.finalproject.ui.update_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.update_listing.repository.FirstUpdateRepository
import com.example.finalproject.ui.update_listing.viewModel.FirstUpdateViewModel

class FirstUpdateFactory(
    private val firstUpdateRepository: FirstUpdateRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstUpdateViewModel::class.java)){
            return FirstUpdateViewModel(firstUpdateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}