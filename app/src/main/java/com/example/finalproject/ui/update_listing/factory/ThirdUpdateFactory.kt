package com.example.finalproject.ui.update_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.update_listing.repository.ThirdUpdateRepository
import com.example.finalproject.ui.update_listing.viewModel.ThirdUpdateViewModel

class ThirdUpdateFactory(
    private val thirdUpdateRepository: ThirdUpdateRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThirdUpdateViewModel::class.java)){
            return ThirdUpdateViewModel(thirdUpdateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}