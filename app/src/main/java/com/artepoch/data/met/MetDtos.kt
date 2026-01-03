// File: app/src/main/java/com/artepoch/data/met/MetDtos.kt
package com.artepoch.data.met

import com.squareup.moshi.Json

data class MetSearchResponse(
    val total: Int = 0,
    val objectIDs: List<Int>? = null
)

data class MetObjectDto(
    val objectID: Int,

    val title: String?,
    val artistDisplayName: String?,

    val objectDate: String?,
    val objectBeginDate: Int?,
    val objectEndDate: Int?,

    val medium: String?,
    val classification: String?,

    val primaryImage: String?,
    val primaryImageSmall: String?,

    @Json(name = "objectURL")
    val objectUrl: String?
)
