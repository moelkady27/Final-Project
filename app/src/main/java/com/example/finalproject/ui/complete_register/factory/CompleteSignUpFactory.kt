package com.example.finalproject.ui.complete_register.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.complete_register.repository.CompleteSignUpRepository
import com.example.finalproject.ui.complete_register.viewModels.CompleteSignUpViewModel

class CompleteSignUpFactory (
    private val completeSignUpRepository: CompleteSignUpRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompleteSignUpViewModel::class.java)){
            return CompleteSignUpViewModel(completeSignUpRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}