package com.example.finalproject.ui.favourite.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.FirstCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.FirstCompleteViewModel
import com.example.finalproject.ui.favourite.repository.AddToFavouritesRepository
import com.example.finalproject.ui.favourite.viewModel.AddToFavouritesViewModel

class AddToFavouritesFactory(
    private val addToFavouritesRepository: AddToFavouritesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddToFavouritesViewModel::class.java)){
            return AddToFavouritesViewModel(addToFavouritesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}