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
    val selectedArtist: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null,

    val artists: List<String> = emptyList(),
    val results: List<Artwork> = emptyList(),
    val selectedArtwork: Artwork? = null,

    // Pagination
    val currentPage: Int = 0,
    val hasMore: Boolean = false
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

    fun loadArtists() {
        val movement = _state.value.selectedMovement ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                artists = emptyList()
            )

            val result = runCatching {
                repository.getArtistsByMovement(movement = movement)
            }

            _state.value = _state.value.copy(
                isLoading = false,
                artists = result.getOrDefault(emptyList()),
                error = result.exceptionOrNull()?.localizedMessage
            )
        }
    }

    fun selectArtist(artist: String) {
        _state.value = _state.value.copy(
            selectedArtist = artist,
            currentPage = 0,
            results = emptyList()
        )
    }

    fun loadResults(page: Int = 0) {
        val movement = _state.value.selectedMovement ?: return
        val artist = _state.value.selectedArtist ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            val result = runCatching {
                repository.searchArtworksByArtist(
                    movement = movement,
                    artist = artist,
                    page = page,
                    pageSize = 10
                )
            }

            _state.value = _state.value.copy(
                isLoading = false,
                results = if (page == 0) result.getOrDefault(emptyList())
                         else _state.value.results + result.getOrDefault(emptyList()),
                currentPage = page,
                hasMore = result.getOrDefault(emptyList()).size >= 10,
                error = result.exceptionOrNull()?.localizedMessage
            )
        }
    }

    fun loadNextPage() {
        if (!_state.value.isLoading && _state.value.hasMore) {
            loadResults(_state.value.currentPage + 1)
        }
    }

    fun openArtwork(artwork: Artwork) {
        _state.value = _state.value.copy(selectedArtwork = artwork)
    }

    fun clearSelectedArtwork() {
        _state.value = _state.value.copy(selectedArtwork = null)
    }

    // Global artist arama
    suspend fun searchArtists(query: String): List<String> {
        return runCatching {
            repository.searchArtists(query)
        }.getOrDefault(emptyList())
    }

    // Global artist bazlı eser arama
    suspend fun getArtworksByArtist(artist: String, page: Int = 0): List<Artwork> {
        return runCatching {
            repository.getArtworksByArtist(artist, page, pageSize = 10)
        }.getOrDefault(emptyList())
    }
}