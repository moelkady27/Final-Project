package com.example.finalproject.ui.booking.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.booking.repository.CancelBookRepository
import com.example.finalproject.ui.booking.viewModel.CancelViewModel

class CancelFactory(
    private val cancelBookRepository: CancelBookRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CancelViewModel::class.java)){
            return CancelViewModel(cancelBookRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}