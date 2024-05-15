package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.FirstCompleteRepository
import com.example.finalproject.ui.add_listing.repository.SecondCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.FirstCompleteViewModel
import com.example.finalproject.ui.add_listing.viewModel.SecondCompleteViewModel

class SecondCompleteFactory(
    private val secondCompleteRepository: SecondCompleteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondCompleteViewModel::class.java)){
            return SecondCompleteViewModel(secondCompleteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}