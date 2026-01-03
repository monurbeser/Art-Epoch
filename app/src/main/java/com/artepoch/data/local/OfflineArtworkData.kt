package com.artepoch.data.local

import android.content.Context
import com.artepoch.domain.model.Artwork
import org.json.JSONArray
import java.io.IOException

/**
 * Offline artwork verilerini assets klasöründen yükler.
 */
class OfflineArtworkData(private val context: Context) {

    private var cachedArtworks: List<Artwork>? = null

    /**
     * Assets klasöründeki artworks.json dosyasından tüm eserleri yükler.
     */
    fun loadAllArtworks(): List<Artwork> {
        // Cache kontrolü - bir kez yükle
        if (cachedArtworks != null) {
            return cachedArtworks!!
        }

        return try {
            val json = context.assets.open("artworks.json").bufferedReader().use {
                it.readText()
            }

            val jsonArray = JSONArray(json)
            val artworks = mutableListOf<Artwork>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                artworks.add(
                    Artwork(
                        id = obj.getInt("id"),
                        title = obj.getString("title"),
                        artist = obj.getString("artist"),
                        beginYear = null,
                        endYear = null,
                        displayDate = obj.optString("year", null),
                        medium = null,
                        classification = obj.getString("movement"),
                        imageUrl = obj.getString("imageUrl"),
                        museumUrl = null
                    )
                )
            }

            cachedArtworks = artworks
            artworks

        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Belirli bir movement için eserleri filtreler.
     */
    fun getArtworksByMovement(movementLabel: String): List<Artwork> {
        return loadAllArtworks().filter { it.classification == movementLabel }
    }

    /**
     * Belirli bir movement için unique artist listesini döndürür.
     */
    fun getArtistsByMovement(movementLabel: String): List<String> {
        return getArtworksByMovement(movementLabel)
            .map { it.artist }
            .distinct()
            .sorted()
    }

    /**
     * Belirli bir artist ve movement için eserleri döndürür (pagination destekli).
     */
    fun getArtworksByArtistAndMovement(
        artist: String,
        movementLabel: String,
        page: Int = 0,
        pageSize: Int = 10
    ): List<Artwork> {
        val allArtworks = getArtworksByMovement(movementLabel)
            .filter { it.artist == artist }

        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, allArtworks.size)

        return if (startIndex >= allArtworks.size) {
            emptyList()
        } else {
            allArtworks.subList(startIndex, endIndex)
        }
    }

    /**
     * Artist ismine göre arama yapar (tüm movements genelinde).
     */
    fun searchArtists(query: String): List<String> {
        val allArtists = loadAllArtworks()
            .map { it.artist }
            .distinct()

        return if (query.isBlank()) {
            allArtists.sorted()
        } else {
            allArtists.filter { it.contains(query, ignoreCase = true) }.sorted()
        }
    }

    /**
     * Artist'e ait tüm eserleri döndürür (tüm movements genelinde).
     */
    fun getArtworksByArtist(artist: String, page: Int = 0, pageSize: Int = 10): List<Artwork> {
        val allArtworks = loadAllArtworks().filter { it.artist == artist }

        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, allArtworks.size)

        return if (startIndex >= allArtworks.size) {
            emptyList()
        } else {
            allArtworks.subList(startIndex, endIndex)
        }
    }
}
