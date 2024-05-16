package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.ThirdCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.ThirdCompleteViewModel

class ThirdCompleteFactory(
    private val thirdCompleteRepository: ThirdCompleteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThirdCompleteViewModel::class.java)) {
            return ThirdCompleteViewModel(thirdCompleteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}