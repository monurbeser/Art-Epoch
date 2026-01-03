// File: app/src/main/java/com/artepoch/data/wiki/WikiDtos.kt
package com.artepoch.data.wiki

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikiResponse(
    val query: WikiQuery?
)

@JsonClass(generateAdapter = true)
data class WikiQuery(
    val pages: Map<String, WikiPage>?
)

@JsonClass(generateAdapter = true)
data class WikiPage(
    val pageid: Int?,
    val title: String?,
    val imageinfo: List<WikiImageInfo>?
)

@JsonClass(generateAdapter = true)
data class WikiImageInfo(
    val url: String?,
    val thumburl: String?,
    val extmetadata: WikiExtMetadata?
)

@JsonClass(generateAdapter = true)
data class WikiExtMetadata(
    @Json(name = "ObjectName")
    val objectName: WikiMetaValue?,

    @Json(name = "Artist")
    val artist: WikiMetaValue?,

    @Json(name = "DateTimeOriginal")
    val dateTimeOriginal: WikiMetaValue?,

    @Json(name = "ImageDescription")
    val imageDescription: WikiMetaValue?
)

@JsonClass(generateAdapter = true)
data class WikiMetaValue(
    val value: String?
)