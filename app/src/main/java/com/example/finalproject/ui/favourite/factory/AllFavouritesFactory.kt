package com.example.finalproject.ui.favourite.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.favourite.repository.AllFavouritesRepository
import com.example.finalproject.ui.favourite.viewModel.AllFavouritesViewModel

class AllFavouritesFactory(
    private val allFavouritesRepository: AllFavouritesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllFavouritesViewModel::class.java)){
            return AllFavouritesViewModel(allFavouritesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}