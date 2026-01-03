// File: app/src/main/java/com/artepoch/data/repo/ArtRepository.kt
package com.artepoch.data.repo

import android.util.Log
import com.artepoch.data.wiki.WikiApi
import com.artepoch.domain.model.Artwork
import com.artepoch.domain.model.Movement

class ArtRepository(
    private val wikiApi: WikiApi
) {

    // Tüm artwork'leri çek ve sanatçılara göre grupla
    suspend fun getArtistsByMovement(movement: Movement): List<String> {
        Log.d("ArtRepository", "Fetching artists for: ${movement.wikiCategory}")

        return try {
            val response = wikiApi.getCategoryImages(
                categoryTitle = "Category:${movement.wikiCategory}",
                limit = 500 // Daha fazla veri çekelim ki tüm sanatçıları bulalım
            )

            val pages = response.query?.pages?.values ?: emptyList()

            // Sanatçıları topla ve grupla
            val artists = pages.mapNotNull { page ->
                val imageInfo = page.imageinfo?.firstOrNull() ?: return@mapNotNull null
                val thumbUrl = imageInfo.thumburl ?: imageInfo.url ?: return@mapNotNull null

                // .svg ve .tif dosyalarını atla
                if (thumbUrl.endsWith(".svg") || thumbUrl.contains(".tif")) {
                    return@mapNotNull null
                }

                val metadata = imageInfo.extmetadata
                val artist = metadata?.artist?.value
                    ?.replace(Regex("<[^>]*>"), "")
                    ?.trim()
                    ?: "Unknown"

                artist
            }
            .distinct()
            .sorted()

            Log.d("ArtRepository", "Found ${artists.size} unique artists")
            artists

        } catch (e: Exception) {
            Log.e("ArtRepository", "Failed to fetch artists: ${e.message}", e)
            throw e
        }
    }

    // Belirli bir sanatçının eserlerini pagination ile getir
    suspend fun searchArtworksByArtist(
        movement: Movement,
        artist: String,
        page: Int = 0,
        pageSize: Int = 10
    ): List<Artwork> {
        Log.d("ArtRepository", "Searching artworks for artist: $artist (page: $page)")

        return try {
            // Daha fazla veri çekelim pagination için
            val response = wikiApi.getCategoryImages(
                categoryTitle = "Category:${movement.wikiCategory}",
                limit = 500
            )

            val pages = response.query?.pages?.values ?: emptyList()

            val allArtworks = pages.mapNotNull { page ->
                val imageInfo = page.imageinfo?.firstOrNull() ?: return@mapNotNull null
                val thumbUrl = imageInfo.thumburl ?: imageInfo.url ?: return@mapNotNull null

                // .svg ve .tif dosyalarını atla
                if (thumbUrl.endsWith(".svg") || thumbUrl.contains(".tif")) {
                    return@mapNotNull null
                }

                val metadata = imageInfo.extmetadata

                // HTML taglerini temizle
                val title = metadata?.objectName?.value
                    ?.replace(Regex("<[^>]*>"), "")
                    ?.trim()
                    ?: page.title?.removePrefix("File:")?.substringBeforeLast(".")
                    ?: "Untitled"

                val pageArtist = metadata?.artist?.value
                    ?.replace(Regex("<[^>]*>"), "")
                    ?.trim()
                    ?: "Unknown"

                // Sadece seçilen sanatçının eserlerini al
                if (pageArtist != artist) return@mapNotNull null

                Artwork(
                    id = page.pageid ?: 0,
                    title = title,
                    artist = pageArtist,
                    beginYear = null,
                    endYear = null,
                    displayDate = metadata?.dateTimeOriginal?.value,
                    medium = null,
                    classification = movement.label,
                    imageUrl = thumbUrl,
                    museumUrl = "https://commons.wikimedia.org/wiki/${page.title?.replace(" ", "_")}"
                )
            }

            // Pagination uygula
            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, allArtworks.size)

            if (startIndex >= allArtworks.size) {
                emptyList()
            } else {
                val pagedResults = allArtworks.subList(startIndex, endIndex)
                Log.d("ArtRepository", "Returning ${pagedResults.size} artworks for page $page")
                pagedResults
            }

        } catch (e: Exception) {
            Log.e("ArtRepository", "Search failed: ${e.message}", e)
            throw e
        }
    }
}