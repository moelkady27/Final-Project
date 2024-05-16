package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.FirstCompleteRepository
import com.example.finalproject.ui.add_listing.repository.FourthCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.FirstCompleteViewModel
import com.example.finalproject.ui.add_listing.viewModel.FourthCompleteViewModel

class FourthCompleteFactory(
    private val fourthCompleteRepository: FourthCompleteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FourthCompleteViewModel::class.java)){
            return FourthCompleteViewModel(fourthCompleteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}