package com.example.finalproject.ui.register.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.register.repository.SignUpRepository
import com.example.finalproject.ui.register.viewModels.SignUpViewModel

class SignUpViewModelFactory(
    private val signUpRepository: SignUpRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(signUpRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}