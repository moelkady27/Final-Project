package com.example.finalproject.ui.profile.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.profile.repository.EditProfileRepository
import com.example.finalproject.ui.profile.viewModels.EditProfileViewModel

class EditProfileFactory(
    private val editProfileRepository: EditProfileRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)){
            return EditProfileViewModel(editProfileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}