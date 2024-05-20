package com.example.finalproject.ui.favourite.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.favourite.repository.DeleteAllFavouriteRepository
import com.example.finalproject.ui.favourite.viewModel.DeleteAllFavouriteViewModel

class DeleteAllFavouriteFactory(
    private val deleteAllFavouriteRepository: DeleteAllFavouriteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteAllFavouriteViewModel::class.java)){
            return DeleteAllFavouriteViewModel(deleteAllFavouriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}