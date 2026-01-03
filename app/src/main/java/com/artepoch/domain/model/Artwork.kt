// File: app/src/main/java/com/artepoch/domain/model/Artwork.kt
package com.artepoch.domain.model

data class Artwork(
    val id: Int,
    val title: String,
    val artist: String,

    val beginYear: Int?,
    val endYear: Int?,
    val displayDate: String?,

    val medium: String?,
    val classification: String?,

    val imageUrl: String?,
    val museumUrl: String?
)
