package com.example.finalproject.ui.home.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import com.example.finalproject.ui.home.repository.HomePopularEstatesRepository
import com.example.finalproject.ui.home.viewModel.HomeFeaturedEstatesViewModel
import com.example.finalproject.ui.home.viewModel.HomePopularEstatesViewModel

class HomePopularEstatesFactory(
    private val homePopularEstatesRepository: HomePopularEstatesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomePopularEstatesViewModel::class.java)){
            return HomePopularEstatesViewModel(homePopularEstatesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
