package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.UpdatePriceRepository
import com.example.finalproject.ui.add_listing.viewModel.UpdatePriceViewModel

class UpdatePriceFactory(
    private val updatePriceRepository: UpdatePriceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdatePriceViewModel::class.java)){
            return UpdatePriceViewModel(updatePriceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}