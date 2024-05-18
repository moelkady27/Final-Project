package com.example.finalproject.ui.home.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import com.example.finalproject.ui.home.viewModel.HomeFeaturedEstatesViewModel

class HomeFeaturedEstatesFactory(
    private val homeFeaturedEstatesRepository: HomeFeaturedEstatesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFeaturedEstatesViewModel::class.java)){
            return HomeFeaturedEstatesViewModel(homeFeaturedEstatesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
