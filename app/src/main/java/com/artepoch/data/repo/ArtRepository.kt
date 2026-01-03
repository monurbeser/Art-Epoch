// File: app/src/main/java/com/artepoch/data/repo/ArtRepository.kt
package com.artepoch.data.repo

import android.util.Log
import com.artepoch.data.wiki.WikiApi
import com.artepoch.domain.model.Artwork
import com.artepoch.domain.model.Movement

class ArtRepository(
    private val wikiApi: WikiApi
) {

    suspend fun searchArtworks(
        movement: Movement,
        limit: Int = 30
    ): List<Artwork> {

        Log.d("ArtRepository", "Searching Wikimedia for: ${movement.wikiCategory}")

        return try {
            val response = wikiApi.getCategoryImages(
                categoryTitle = "Category:${movement.wikiCategory}",
                limit = limit
            )

            val pages = response.query?.pages?.values ?: emptyList()

            Log.d("ArtRepository", "Found ${pages.size} images")

            pages.mapNotNull { page ->
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

                val artist = metadata?.artist?.value
                    ?.replace(Regex("<[^>]*>"), "")
                    ?.trim()
                    ?: "Unknown artist"

                Artwork(
                    id = page.pageid ?: 0,
                    title = title,
                    artist = artist,
                    beginYear = null,
                    endYear = null,
                    displayDate = metadata?.dateTimeOriginal?.value,
                    medium = null,
                    classification = movement.label,
                    imageUrl = thumbUrl,
                    museumUrl = "https://commons.wikimedia.org/wiki/${page.title?.replace(" ", "_")}"
                )
            }
        } catch (e: Exception) {
            Log.e("ArtRepository", "Search failed: ${e.message}", e)
            throw e
        }
    }
}