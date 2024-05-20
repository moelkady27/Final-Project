package com.example.finalproject.ui.favourite.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.favourite.repository.DeleteFavouriteRepository
import com.example.finalproject.ui.favourite.viewModel.DeleteFavouriteViewModel

class DeleteFavouriteFactory(
    private val deleteFavouriteRepository: DeleteFavouriteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteFavouriteViewModel::class.java)){
            return DeleteFavouriteViewModel(deleteFavouriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}