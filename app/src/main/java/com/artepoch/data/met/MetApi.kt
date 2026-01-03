// File: app/src/main/java/com/artepoch/data/met/MetApi.kt
package com.artepoch.data.met

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetApi {

    // Example:
    // /public/collection/v1/search?hasImages=true&q=Impressionism
    @GET("public/collection/v1/search")
    suspend fun search(
        @Query("q") q: String,
        @Query("hasImages") hasImages: Boolean = true
    ): MetSearchResponse

    // Example:
    // /public/collection/v1/objects/436535
    @GET("public/collection/v1/objects/{id}")
    suspend fun getObject(
        @Path("id") id: Int
    ): MetObjectDto
}
