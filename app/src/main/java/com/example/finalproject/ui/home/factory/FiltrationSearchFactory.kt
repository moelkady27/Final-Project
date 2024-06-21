package com.example.finalproject.ui.home.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.home.repository.FiltrationSearchRepository
import com.example.finalproject.ui.home.viewModel.FiltrationSearchViewModel

class FiltrationSearchFactory(
    private val filtrationSearchRepository: FiltrationSearchRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiltrationSearchViewModel::class.java)){
            return FiltrationSearchViewModel(filtrationSearchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
