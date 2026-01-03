// File: app/src/main/java/com/artepoch/viewmodel/ArtViewModelFactory.kt
package com.artepoch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artepoch.data.repo.ArtRepository

class ArtViewModelFactory(
    private val repository: ArtRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtViewModel::class.java)) {
            return ArtViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
