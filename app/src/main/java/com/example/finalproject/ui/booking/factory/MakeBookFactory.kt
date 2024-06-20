package com.example.finalproject.ui.booking.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.booking.repository.MakeBookRepository
import com.example.finalproject.ui.booking.viewModel.MakeBookViewModel

class MakeBookFactory(
    private val makeBookRepository: MakeBookRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MakeBookViewModel::class.java)){
            return MakeBookViewModel(makeBookRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}