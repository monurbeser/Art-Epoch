// File: app/src/main/java/com/artepoch/viewmodel/ArtViewModel.kt
package com.artepoch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artepoch.data.repo.ArtRepository
import com.artepoch.domain.model.Artwork
import com.artepoch.domain.model.Movement
import com.artepoch.domain.model.Period
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ArtUiState(
    val selectedPeriod: Period? = null,
    val selectedMovement: Movement? = null,

    val isLoading: Boolean = false,
    val error: String? = null,

    val results: List<Artwork> = emptyList(),
    val selectedArtwork: Artwork? = null
)

class ArtViewModel(
    private val repository: ArtRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ArtUiState())
    val state: StateFlow<ArtUiState> = _state

    fun selectPeriod(period: Period) {
        _state.value = _state.value.copy(selectedPeriod = period)
    }

    fun selectMovement(movement: Movement) {
        _state.value = _state.value.copy(selectedMovement = movement)
    }

    fun loadResults() {
        val movement = _state.value.selectedMovement ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                results = emptyList()
            )

            val result = runCatching {
                repository.searchArtworks(movement = movement)
            }

            _state.value = _state.value.copy(
                isLoading = false,
                results = result.getOrDefault(emptyList()),
                error = result.exceptionOrNull()?.localizedMessage
            )
        }
    }

    fun openArtwork(artwork: Artwork) {
        _state.value = _state.value.copy(selectedArtwork = artwork)
    }

    fun clearSelectedArtwork() {
        _state.value = _state.value.copy(selectedArtwork = null)
    }
}