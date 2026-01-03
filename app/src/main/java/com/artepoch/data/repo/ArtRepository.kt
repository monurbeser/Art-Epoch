// File: app/src/main/java/com/artepoch/data/repo/ArtRepository.kt
package com.artepoch.data.repo

import android.util.Log
import com.artepoch.data.local.OfflineArtworkData
import com.artepoch.domain.model.Artwork
import com.artepoch.domain.model.Movement

class ArtRepository(
    private val offlineData: OfflineArtworkData
) {

    // Tüm artwork'leri çek ve sanatçılara göre grupla (OFFLINE)
    suspend fun getArtistsByMovement(movement: Movement): List<String> {
        Log.d("ArtRepository", "Loading artists for: ${movement.label} (offline)")

        return try {
            val artists = offlineData.getArtistsByMovement(movement.label)
            Log.d("ArtRepository", "Found ${artists.size} unique artists")
            artists
        } catch (e: Exception) {
            Log.e("ArtRepository", "Failed to load artists: ${e.message}", e)
            emptyList()
        }
    }

    // Belirli bir sanatçının eserlerini pagination ile getir (OFFLINE)
    suspend fun searchArtworksByArtist(
        movement: Movement,
        artist: String,
        page: Int = 0,
        pageSize: Int = 10
    ): List<Artwork> {
        Log.d("ArtRepository", "Loading artworks for artist: $artist (page: $page, offline)")

        return try {
            val artworks = offlineData.getArtworksByArtistAndMovement(
                artist = artist,
                movementLabel = movement.label,
                page = page,
                pageSize = pageSize
            )
            Log.d("ArtRepository", "Returning ${artworks.size} artworks for page $page")
            artworks
        } catch (e: Exception) {
            Log.e("ArtRepository", "Search failed: ${e.message}", e)
            emptyList()
        }
    }

    // Artist arama (tüm movements genelinde)
    suspend fun searchArtists(query: String): List<String> {
        Log.d("ArtRepository", "Searching artists with query: $query")
        return try {
            offlineData.searchArtists(query)
        } catch (e: Exception) {
            Log.e("ArtRepository", "Artist search failed: ${e.message}", e)
            emptyList()
        }
    }

    // Belirli bir artist'in tüm eserlerini getir (tüm movements genelinde)
    suspend fun getArtworksByArtist(
        artist: String,
        page: Int = 0,
        pageSize: Int = 10
    ): List<Artwork> {
        Log.d("ArtRepository", "Loading all artworks for artist: $artist (page: $page)")
        return try {
            offlineData.getArtworksByArtist(artist, page, pageSize)
        } catch (e: Exception) {
            Log.e("ArtRepository", "Failed to load artworks: ${e.message}", e)
            emptyList()
        }
    }
}