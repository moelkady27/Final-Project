package com.example.finalproject.ui.booking.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.booking.repository.AcceptRepository
import com.example.finalproject.ui.booking.viewModel.AcceptViewModel

class AcceptFactory(
    private val acceptRepository: AcceptRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcceptViewModel::class.java)){
            return AcceptViewModel(acceptRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}