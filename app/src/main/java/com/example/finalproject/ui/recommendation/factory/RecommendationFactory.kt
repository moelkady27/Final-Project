package com.example.finalproject.ui.recommendation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.recommendation.repository.RecommendationRepository
import com.example.finalproject.ui.recommendation.viewModel.RecommendationViewModel

class RecommendationFactory(
    private val recommendationRepository: RecommendationRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecommendationViewModel::class.java)){
            return RecommendationViewModel(recommendationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}