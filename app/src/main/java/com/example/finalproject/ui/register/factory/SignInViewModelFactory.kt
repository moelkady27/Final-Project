package com.example.finalproject.ui.register.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.register.repository.SignInRepository
import com.example.finalproject.ui.register.viewModels.SignInViewModel

class SignInViewModelFactory(
    private val signInRepository: SignInRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)){
            return SignInViewModel(signInRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}