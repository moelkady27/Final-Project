package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.GetPriceRepository
import com.example.finalproject.ui.add_listing.viewModel.GetPriceViewModel

class GetPriceFactory(
    private val getPriceRepository: GetPriceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetPriceViewModel::class.java)){
            return GetPriceViewModel(getPriceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}