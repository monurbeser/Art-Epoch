// File: app/src/main/java/com/artepoch/data/wiki/WikiApi.kt
package com.artepoch.data.wiki

import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {

    @GET("w/api.php")
    suspend fun getCategoryImages(
        @Query("action") action: String = "query",
        @Query("format") format: String = "json",
        @Query("generator") generator: String = "categorymembers",
        @Query("gcmtitle") categoryTitle: String,
        @Query("gcmtype") type: String = "file",
        @Query("gcmlimit") limit: Int = 50,
        @Query("prop") prop: String = "imageinfo",
        @Query("iiprop") iiProp: String = "url|extmetadata",
        @Query("iiurlwidth") urlWidth: Int = 400
    ): WikiResponse
}