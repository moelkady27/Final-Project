package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.CreateResidenceRepository
import com.example.finalproject.ui.add_listing.repository.FirstCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.CreateResidenceViewModel
import com.example.finalproject.ui.add_listing.viewModel.FirstCompleteViewModel

class FirstCompleteFactory(
    private val firstCompleteRepository: FirstCompleteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstCompleteViewModel::class.java)){
            return FirstCompleteViewModel(firstCompleteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}